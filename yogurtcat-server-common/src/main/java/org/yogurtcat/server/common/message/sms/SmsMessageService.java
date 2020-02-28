package org.yogurtcat.server.common.message.sms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yogurtcat.server.common.constant.MessageType;
import org.yogurtcat.server.common.message.domain.MessageConfig;
import org.yogurtcat.server.common.message.service.MessageConfigService;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信发送服务类
 * @author s
 *
 */
@Slf4j
@Service
public class SmsMessageService {
	
	private static final String CODE = "Code";
	
	private static final String OK = "OK";
	
	@Autowired
	private MessageConfigService messageConfigService;

	/**
	 * 短信发送接口
	 * @param phoneNumber  接收短信的手机号码，多个手机号码以英文逗号（,）分隔
	 * @param signName     短信签名名称，可在控制台签名管理页面签名名称一列查看
	 * @param templateCode 短信模板ID，可在控制台模板管理页面模板CODE一列查看
	 * @param paramMap     短信模板变量对应的实际值
	 * @throws Exception
	 */
	public void sendSms(String phoneNumber, String signName, String templateCode, 
			Map<String, String> paramMap) throws Exception {
		
		MessageConfig config = messageConfigService.findByType(MessageType.短信);
		
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyId(), config.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        Gson gson = new Gson();
        if (!CollectionUtils.isEmpty(paramMap)) {
        	request.putQueryParameter("TemplateParam", gson.toJson(paramMap));
        }
        try {
        	CommonResponse result = client.getCommonResponse(request);
        	@SuppressWarnings("unchecked")
			Map<String, String> map = gson.fromJson(result.getData(), Map.class);
        	//发送失败
        	if (!OK.equals(map.get(CODE))) {
        		log.error("Send sms message error: " + map.get("Message"));
        	}
        } catch (Exception e) {
            log.error("SendSms error", e);
            throw e;
        }
	}
}
