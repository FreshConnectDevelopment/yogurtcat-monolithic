package org.yogurtcat.server.modules.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * RabbitMQ消息处理
 * 
 * @author heaven
 *
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "spring.rabbitmq", name = "enabled")
public class RabbitMqProcess {
	
	@RabbitListener(queues = "someQueue")
	public void processMessage(String content) {
		log.info(content);
	}

}
