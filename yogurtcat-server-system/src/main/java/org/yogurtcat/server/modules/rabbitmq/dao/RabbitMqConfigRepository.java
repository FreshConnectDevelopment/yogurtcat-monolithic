package org.yogurtcat.server.modules.rabbitmq.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.modules.rabbitmq.domain.RabbitMqConfig;

/**
 * RabbitMQ配置dao
 * @author s
 *
 */
public interface RabbitMqConfigRepository extends Repository<RabbitMqConfig, Long>, JpaSpecificationExecutor<RabbitMqConfig> {

	/**
	 * 新增或修改
	 * @param payment
	 * @return
	 */
	RabbitMqConfig save(RabbitMqConfig payment);

	/**
	 * 查询按id排序的第一个配置
	 * @return
	 */
	RabbitMqConfig findTopByOrderById();

	/**
	 * 删除
	 * @param rabbitMqConfig
	 */
	void delete(RabbitMqConfig rabbitMqConfig);
}
