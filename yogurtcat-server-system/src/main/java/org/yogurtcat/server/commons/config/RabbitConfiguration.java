package org.yogurtcat.server.commons.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yogurtcat.server.modules.rabbitmq.domain.RabbitMqConfig;
import org.yogurtcat.server.modules.rabbitmq.service.RabbitMqConfigService;

/**
 * rabbitmq配置
 * 
 * @author heaven
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq.db", name = "enabled")
public class RabbitConfiguration {

	@Autowired
	private RabbitMqConfigService rabbitMqConfigService;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		RabbitMqConfig config = rabbitMqConfigService.find();
		connectionFactory.setHost(config.getHost());
		connectionFactory.setPort(config.getPort());
		connectionFactory.setUsername(config.getUsername());
		connectionFactory.setPassword(config.getPassword());
		return connectionFactory;
	}
}
