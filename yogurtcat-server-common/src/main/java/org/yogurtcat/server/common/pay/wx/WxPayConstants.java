package org.yogurtcat.server.common.pay.wx;

import org.apache.http.client.HttpClient;

/**
 * 支付相关常量
 * @author s
 *
 */
public class WxPayConstants {

    public enum SignType {
    	/**
    	 * MD5
    	 */
        MD5, 
        /**
         * HMACSHA256
         */
        HMACSHA256
    }

    public static final String FAIL     = "FAIL";
    public static final String SUCCESS  = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String RETURN_CODE = "return_code";
    public static final String CHECK_NAME = "check_name";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String WXPAYSDK_VERSION = "WXPaySDK/3.0.9";
    public static final String USER_AGENT = WXPAYSDK_VERSION +
            " (" + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version") +
            ") Java/" + System.getProperty("java.version") + " HttpClient/" + HttpClient.class.getPackage().getImplementationVersion();

    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String ORDERQUERY_URL   = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String REFUND_URL       = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String REFUNDQUERY_URL  = "https://api.mch.weixin.qq.com/pay/refundquery";
	public static final String TRANSFERS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";


}

