package org.yogurtcat.server.modules.rabbitmq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.modules.rabbitmq.domain.RabbitMqConfig;
import org.yogurtcat.server.modules.rabbitmq.dao.RabbitMqConfigRepository;

/**
 * RabbitMq配置serviceImpl
 * @author s
 *
 */
@Service
public class RabbitMqConfigServiceImpl implements RabbitMqConfigService {
	
	@Autowired
	private RabbitMqConfigRepository rabbitMqConfigRepository;


	@Override
	public RabbitMqConfig save(RabbitMqConfig data) {
		return rabbitMqConfigRepository.save(data);
	}

	@Override
	public RabbitMqConfig find() {
		return rabbitMqConfigRepository.findTopByOrderById();
	}

	@Override
	public void delete(RabbitMqConfig rabbitMqConfig) {
		rabbitMqConfigRepository.delete(rabbitMqConfig);
	}
}
