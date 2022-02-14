package org.yogurtcat.server.common.pay;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.yogurtcat.server.common.constant.PaymentType;
import org.yogurtcat.server.common.pay.wx.WxPayConstants;
import org.yogurtcat.server.common.pay.wx.WxPayConstants.SignType;
import org.yogurtcat.server.common.pay.wx.WxPayRequest;
import org.yogurtcat.server.common.pay.wx.WxPayUtil;
import org.yogurtcat.server.common.payment.domain.Payment;
import org.yogurtcat.server.common.payment.service.PaymentService;

import io.netty.util.internal.StringUtil;

/**
 * 微信支付相关工具类
 * 
 * @author s
 *
 */
@Component
public class WxPay {

    @Autowired
    private PaymentService paymentService;

    /**
     * 签名类型
     * 默认为MD5，支持HMAC-SHA256和MD5
     */
    private SignType signType = SignType.MD5;

    @Autowired
    private WxPayRequest wxPayRequest;

    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     * @param config
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData, Payment config) throws Exception {
        reqData.put("appid", config.getAppId());
        reqData.put("mch_id", config.getMchId());
        reqData.put("nonce_str", WxPayUtil.generateNonceStr());
        // 默认MD5，非必填
        if (SignType.HMACSHA256.equals(this.signType)) {
            reqData.put("sign_type", WxPayConstants.HMACSHA256);
        }
        reqData.put("sign", WxPayUtil.generateSignature(reqData, config.getApiSecret(), this.signType));
        return reqData;
    }

    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     *
     * @param reqData 向wxpay post的请求数据
     * @param config
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isResponseSignatureValid(Map<String, String> reqData, Payment config) throws Exception {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return WxPayUtil.isSignatureValid(reqData, config.getApiSecret(), this.signType);
    }

    /**
     * 判断支付结果通知中的sign是否有效
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        String signTypeInData = reqData.get(WxPayConstants.FIELD_SIGN_TYPE);
        SignType signType;
        if (signTypeInData == null) {
            signType = SignType.MD5;
        } else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = SignType.MD5;
            } else if (WxPayConstants.MD5.equals(signTypeInData)) {
                signType = SignType.MD5;
            } else if (WxPayConstants.HMACSHA256.equals(signTypeInData)) {
                signType = SignType.HMACSHA256;
            } else {
                throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
            }
        }
        Payment config = getWxPayment();
        return WxPayUtil.isSignatureValid(reqData, config.getApiSecret(), signType);
    }

    /**
     * 不需要证书的请求
     * 
     * @param urlSuffix        String
     * @param reqData          向wxpay post的请求数据
     * @param config
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs    超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithoutCert(String url, Map<String, String> reqData, Payment config) throws Exception {
        String reqBody = WxPayUtil.mapToXml(reqData);

        String resp = this.wxPayRequest.request(url, reqBody, false, config);
        return resp;
    }

    /**
     * 需要证书的请求
     * 
     * @param urlSuffix        String
     * @param reqData          向wxpay post的请求数据 Map
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs    超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(String url, Map<String, String> reqData, Payment config) throws Exception {
        String reqBody = WxPayUtil.mapToXml(reqData);

        String resp = this.wxPayRequest.request(url, reqBody, true, config);
        return resp;
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * 
     * @param xmlStr API返回的XML格式数据
     * @param config
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr, Payment config) throws Exception {
        String returnCode = StringUtil.EMPTY_STRING;
        Map<String, String> respData = WxPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(WxPayConstants.RETURN_CODE)) {
            returnCode = respData.get(WxPayConstants.RETURN_CODE);
        } else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (returnCode.equals(WxPayConstants.FAIL)) {
            return respData;
        } else if (returnCode.equals(WxPayConstants.SUCCESS)) {
            if (this.isResponseSignatureValid(respData, config)) {
                return respData;
            } else {
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", returnCode, xmlStr));
        }
    }

    /**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付
     * 
     * @param reqData 向wxpay post的请求数据
     *                必须包含notify_url、body、out_trade_no、total_fee、spbill_create_ip、trade_type、openid字段
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
        Payment config = getWxPayment();

        String url = WxPayConstants.UNIFIEDORDER_URL;
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData, config), config);
        return this.processResponseXml(respXml, config);
    }

    /**
     * 作用：查询订单<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * 
     * @param reqData 向wxpay post的请求数据 必须包含out_trade_no
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> orderQuery(Map<String, String> reqData) throws Exception {
        Payment config = getWxPayment();

        String url = WxPayConstants.ORDERQUERY_URL;
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData, config), config);
        return this.processResponseXml(respXml, config);
    }

    /**
     * 作用：申请退款<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付<br>
     * 其他：需要证书
     * 
     * @param reqData 向wxpay post的请求数据
     *                必须包含out_refund_no、out_trade_no、total_fee、refund_fee字段
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> refund(Map<String, String> reqData) throws Exception {
        Payment config = getWxPayment();

        String url = WxPayConstants.REFUND_URL;
        String respXml = this.requestWithCert(url, this.fillRequestData(reqData, config), config);
        return this.processResponseXml(respXml, config);
    }

    /**
     * 作用：退款查询<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * 
     * @param reqData 向wxpay post的请求数据 必须包含out_trade_no
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> refundQuery(Map<String, String> reqData) throws Exception {
        Payment config = getWxPayment();

        String url = WxPayConstants.REFUNDQUERY_URL;
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData, config), config);
        return this.processResponseXml(respXml, config);
    }

    /**
     * 企业付款到零钱
     * 
     * @param reqData 必须包含partner_trade_no、openid、amount、desc、spbill_create_ip字段
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> transfers(Map<String, String> reqData) throws Exception {
        Payment config = getWxPayment();

        String url = WxPayConstants.TRANSFERS_URL;
        if (ObjectUtils.isEmpty(reqData.get(WxPayConstants.CHECK_NAME))) {
            reqData.put(WxPayConstants.CHECK_NAME, "NO_CHECK");
        }
        reqData.put("mch_appid", config.getAppId());
        reqData.put("mchid", config.getMchId());
        reqData.put("nonce_str", WxPayUtil.generateNonceStr());
        reqData.put("sign", WxPayUtil.generateSignature(reqData, config.getApiSecret(), this.signType));
        String respXml = this.requestWithCert(url, reqData, config);
        return this.processResponseXml(respXml, config);
    }

    /**
     * 获取wx支付配置参数
     * 
     * @return
     */
    private Payment getWxPayment() {
        return paymentService.findByType(PaymentType.微信支付);
    }

}
