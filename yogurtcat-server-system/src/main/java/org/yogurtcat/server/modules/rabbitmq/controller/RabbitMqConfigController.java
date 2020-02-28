package org.yogurtcat.server.modules.rabbitmq.controller;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yogurtcat.server.common.rabbit.RabbitService;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.rabbitmq.domain.RabbitMqConfig;
import org.yogurtcat.server.modules.rabbitmq.service.RabbitMqConfigService;

/**
 * RabbitMq配置接口类
 * @author s
 *
 */
@RestController
@RequestMapping("rabbit")
public class RabbitMqConfigController {

	@Autowired
	private RabbitMqConfigService rabbitMqConfigService;

	@Autowired
	private RabbitService rabbitService;
	
	private SimpleMessageListenerContainer listenerContainer;

	/**
	 * 查询rabbitMQ配置
	 * 
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list() throws BusinessException {
		RabbitMqConfig rabbitMqConfig = rabbitMqConfigService.find();
		return ResponseResult.builder().data(rabbitMqConfig).build();
	}

	@PostMapping(path = "update")
	public ResponseResult update(@RequestBody RabbitMqConfig rabbitMqConfig) throws BusinessException {
		rabbitMqConfig = rabbitMqConfigService.save(rabbitMqConfig);
		listenerContainer = rabbitService.replyListenerContainer(
				rabbitMqConfig.getHost(), rabbitMqConfig.getPort(), rabbitMqConfig.getUsername(),
				rabbitMqConfig.getPassword());
		listenerContainer.start();
		return ResponseResult.builder().data(rabbitMqConfig).build();
	}

	@PostMapping(path = "delete", consumes = "application/json")
	public ResponseResult delete(@RequestBody RabbitMqConfig rabbitMqConfig) throws BusinessException {
		rabbitMqConfigService.delete(rabbitMqConfig);
		return ResponseResult.builder().build();
	}

}
