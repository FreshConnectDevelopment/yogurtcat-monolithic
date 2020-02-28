package org.yogurtcat.server.common.message.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.common.constant.MessageType;
import org.yogurtcat.server.common.message.domain.MessageConfig;

/**
 * 消息配置dao
 * @author s
 *
 */
public interface MessageConfigRepository extends Repository<MessageConfig, Long>, JpaSpecificationExecutor<MessageConfig> {
	
	/**
	 * 新增或修改
	 * @param payment
	 * @return
	 */
	MessageConfig save(MessageConfig payment);

	/**
	 * 删除
	 * @param payment
	 */
	void delete(MessageConfig payment);

	/**
	 * 根据type查询
	 * @param type
	 * @return
	 */
	MessageConfig findByType(MessageType type);
}
