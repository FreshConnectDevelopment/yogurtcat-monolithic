package org.yogurtcat.server.common.mq.mqtt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * MQTT客户端管理器
 * 
 * <p>
 * <li>创建MQTT客户端</li>
 * <li>删除MQTT客户端</li>
 * </p>
 * 
 * @author heaven
 *
 */
@Component
@ConditionalOnProperty(prefix = "spring.mqtt.client.manager", name = "enabled")
public class MqttClientManager {

	/**
	 * 客户端列表
	 */
	Map<String, IMqttClient> clients = new ConcurrentHashMap<>();

	/**
	 * 获取客户端
	 * 
	 * @param clientId
	 * @return
	 */
	public IMqttClient getMqttClient(String clientId) {
		return clients.get(clientId);
	}

	/**
	 * 创建客户端
	 * 
	 * @param name
	 * @param password
	 * @param host
	 * @param port
	 * @param clientId
	 * @return
	 * @throws MqttException
	 */
	public IMqttClient createMqttClient(String name, String password, String host, String port, String clientId)
			throws MqttException {
		if (clients.get(clientId) == null || !clients.get(clientId).isConnected()) {
			IMqttClient client = MqttClient.newInstance(name, password, host, port, clientId);
			clients.put(clientId, client);
		}
		return clients.get(clientId);
	}
	
	/**
	 * 删除MQTT客户端
	 * @param clientId
	 * @throws MqttException
	 */
	public void deleteMqttClient(String clientId) throws MqttException {
		IMqttClient iMqttClient = clients.get(clientId);
		if (iMqttClient != null) {
			iMqttClient.disconnect();
			iMqttClient.close();
			clients.remove(clientId);
		}
	}

}
