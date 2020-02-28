package org.yogurtcat.server.common.message.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.common.constant.MessageType;
import org.yogurtcat.server.common.message.dao.MessageConfigRepository;
import org.yogurtcat.server.common.message.domain.MessageConfig;

/**
 * 消息配置serviceImpl
 * @author s
 *
 */
@Service
public class MessageConfigServiceImpl implements MessageConfigService {
	
	@Autowired
	private MessageConfigRepository messageConfigRepository;

	@Override
	public List<MessageConfig> list() {
		List<MessageConfig> page = messageConfigRepository.findAll(null);
		return page;
	}

	@Override
	public MessageConfig save(MessageConfig data) {
		return messageConfigRepository.save(data);
		
	}

	@Override
	public void delete(MessageConfig data) {
		messageConfigRepository.delete(data);
	}

	@Override
	public MessageConfig findByType(MessageType type) {
		return messageConfigRepository.findByType(type);
	}

	
}
