package org.yogurtcat.server.common.mq.mqtt;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * MQTT客户端
 * 
 * @author heaven
 *
 */
public class MqttClient {

	/**
	 * MQTT客户端实例化
	 * 
	 * @param name
	 * @param password
	 * @param host
	 * @param port
	 * @param clientId
	 * @return
	 * @throws MqttException
	 */
	public static IMqttClient newInstance(String name, String password, String host, String port, String clientId)
			throws MqttException {
		DefaultMqttPahoClientFactory pahoClientFactory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions connectionOptions = new MqttConnectOptions();
		connectionOptions.setUserName(name);
		connectionOptions.setPassword(password.toCharArray());
		IMqttClient client = pahoClientFactory.getClientInstance("tcp://" + host + ":" + port, clientId);
		client.connect(connectionOptions);
		return client;
	}

	/**
	 * 添加主題
	 * @param topic
	 * @param userName
	 * @param password
	 * @param hostName
	 * @param portNumber
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void addTopic(String topic, String userName, String password, String hostName,
			int portNumber) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(userName);
		factory.setPassword(password);
		factory.setVirtualHost("/");
		factory.setHost(hostName);
		factory.setPort(portNumber);
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(topic, true, false, false, null);
		channel.close();
		conn.close();
	}
	
	/**
	 * 删除主题
	 * @param topic
	 * @param userName
	 * @param password
	 * @param virtualHost
	 * @param hostName
	 * @param portNumber
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void deleteTopic(String topic, String userName, String password, String hostName,
			int portNumber) throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(userName);
		factory.setPassword(password);
		factory.setVirtualHost("/");
		factory.setHost(hostName);
		factory.setPort(portNumber);
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		channel.queueDelete(topic);
		channel.close();
		conn.close();
	}

	/**
	 * 订阅主题
	 * 
	 * @param client
	 * @param topicFilter
	 * @param messageListener
	 * @throws MqttSecurityException
	 * @throws MqttException
	 */
	public void subscribeTopic(IMqttClient client, String topicFilter, IMqttMessageListener messageListener)
			throws MqttSecurityException, MqttException {
		client.subscribe(topicFilter, messageListener);
	}

	/**
	 * 注销订阅主题
	 * 
	 * @param client
	 * @param topic
	 * @throws MqttSecurityException
	 * @throws MqttException
	 */
	public void unsubscribeTopic(IMqttClient client, String topic) throws MqttSecurityException, MqttException {
		client.unsubscribe(topic);
	}

	/**
	 * 发布消息
	 * 
	 * @param client
	 * @param topic
	 * @param message
	 * @return
	 * @throws MqttException
	 * @throws MqttPersistenceException
	 */
	public void publish(IMqttClient client, String topic, byte[] message)
			throws MqttPersistenceException, MqttException {
		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setPayload(message);
		mqttMessage.setQos(1);
		mqttMessage.setRetained(false);
		client.publish(topic, mqttMessage);
	}

}
