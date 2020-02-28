package org.yogurtcat.server.modules.rabbitmq.service;

import org.yogurtcat.server.modules.rabbitmq.domain.RabbitMqConfig;

/**
 * RabbitMQ配置service
 * @author s
 *
 */
public interface RabbitMqConfigService {

	/**
	 * 新增或修改
	 * @param data
	 * @return
	 */
	RabbitMqConfig save(RabbitMqConfig data);
	
	/**
	 * 查找配置
	 * @param paymentType
	 * @return
	 */
	RabbitMqConfig find();

	/**
	 * 重置
	 * @param rabbitMqConfig
	 */
	void delete(RabbitMqConfig rabbitMqConfig);
}
