package org.yogurtcat.server.modules.messageconfig.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yogurtcat.server.common.constant.Module;
import org.yogurtcat.server.common.message.domain.MessageConfig;
import org.yogurtcat.server.common.message.service.MessageConfigService;
import org.yogurtcat.server.common.message.sms.SmsMessageService;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.monitor.operationlog.annotation.OperationLog;

/**
 * 消息配置接口
 * @author s
 *
 */
@RestController
@RequestMapping("message")
public class MessageConfigController {

	@Autowired
	private MessageConfigService messageConfigService;
	
	@Autowired
	private SmsMessageService smsMessageService;
	
	/**
	 * 初始化全部消息配置方式
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list() throws BusinessException {
		List<MessageConfig> list = messageConfigService.list();
		if(Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		}else {
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode()).message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}
	
	@OperationLog(logCode = "message", module = Module.修改消息工具配置)
	@PostMapping(path = "update")
	public ResponseResult update(@RequestBody MessageConfig data) throws BusinessException, IOException {
		MessageConfig messageConfig = messageConfigService.findByType(data.getType());
		if (null == messageConfig) {
			messageConfig = new MessageConfig();
			messageConfig.setType(data.getType());
		}
		messageConfig.setAccessKeyId(data.getAccessKeyId());
		messageConfig.setAccessKeySecret(data.getAccessKeySecret());
		
		messageConfig = messageConfigService.save(messageConfig);
		return ResponseResult.builder().data(messageConfig).build();
	}
	
	@PostMapping(path = "delete", consumes = "application/json")
	public ResponseResult delete(@RequestBody MessageConfig data) throws BusinessException {
		messageConfigService.delete(data);
		return ResponseResult.builder().build();
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(path = "test", consumes = "application/json")
	public ResponseResult test(@RequestBody Map<String, Object> map) throws BusinessException, Exception {
		smsMessageService.sendSms((String) map.get("phoneNumber"), (String) map.get("signName"), (String) map.get("templateCode"), (Map<String, String>) map.get("paramMap"));
		return ResponseResult.builder().build();
	}
	
}
