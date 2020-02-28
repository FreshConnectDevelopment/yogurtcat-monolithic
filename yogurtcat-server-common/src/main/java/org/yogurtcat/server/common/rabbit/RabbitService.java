package org.yogurtcat.server.common.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * rabbit服务
 * @author sunwei
 *
 */
@Slf4j
@Service
public class RabbitService {

	public ConnectionFactory connectionFactory(String host, int port, String userName, String password) {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(userName);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}
	
    public RabbitTemplate amqpTemplate(String host, int port, String userName, String password) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory(host, port, userName, password));
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        rabbitTemplate.setReplyAddress("someQueue");
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setUseDirectReplyToContainer(false);
        return rabbitTemplate;
    }
	
	public SimpleMessageListenerContainer replyListenerContainer(String host, int port, String userName, String password) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory(host, port, userName, password));
        container.setQueues(new Queue("someQueue"));
        container.setMessageListener(listener());
        return container;
    }     
	
	public MessageListener listener() {
        return new MessageListener() {
            public void onMessage(Message message) {
                log.info("received: " + message);
            }
        };
    }

}
