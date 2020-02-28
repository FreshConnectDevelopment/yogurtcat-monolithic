package org.yogurtcat.server.common.message.service;

import java.util.List;

import org.yogurtcat.server.common.constant.MessageType;
import org.yogurtcat.server.common.message.domain.MessageConfig;

/**
 * 消息配置service
 * @author s
 *
 */
public interface MessageConfigService {

	/**
	 * 查询所有
	 * @return
	 */
	List<MessageConfig> list();

	/**
	 * 新增或更新
	 * @param data
	 * @return
	 */
	MessageConfig save(MessageConfig data);

	/**
	 * 删除
	 * @param data
	 */
	void delete(MessageConfig data);
	
	/**
	 * 根据支付类型查找
	 * @param type
	 * @return
	 */
	MessageConfig findByType(MessageType type);
}
