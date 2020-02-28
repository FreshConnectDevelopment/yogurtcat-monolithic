package org.yogurtcat.server.modules.monitor.operationlog.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.common.utils.StringUtil;
import org.yogurtcat.server.modules.monitor.operationlog.dao.OperationLogRepository;
import org.yogurtcat.server.modules.monitor.operationlog.dao.OperationLogSpec;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLog;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLogVo;

import lombok.extern.slf4j.Slf4j;

/**
 * 管理员操作日志serviceImpl
 * @author s
 *
 */
@Slf4j
@Service
public class OperationLogServiceImpl implements OperationLogService {

	@Autowired
	private OperationLogRepository operationLogRepository;

	@Autowired
	private MessageSource messages;

	@Override
	public Page<OperationLogVo> findAll(Optional<OperationLogVo> condition, Pageable pageable) {

		Page<OperationLog> result = operationLogRepository
				.findAll(OperationLogSpec.builder().condition(condition).build(), pageable);
		List<OperationLogVo> content = result.getContent().stream()
				.map(value -> mapperToOperationLogVo(value, Locale.CHINA)).collect(Collectors.toList());
		return new PageImpl<OperationLogVo>(content, result.getPageable(), result.getTotalElements());
	}

	private OperationLogVo mapperToOperationLogVo(OperationLog operationLog, Locale locale) {
		String logMessage = messages.getMessage(operationLog.getLogCode(), null, Locale.CHINA);
		try {
			logMessage = StringUtil.translate(logMessage, operationLog.getArg());
		} catch (Exception e) {
			log.error("translate error:{0}", e);
		}
		return OperationLogVo.builder().content(logMessage).createTime(operationLog.getCreateTime())
				.id(operationLog.getId()).username(operationLog.getUsername()).ip(operationLog.getIp())
				.module(operationLog.getModule()).result(operationLog.getResult()).build();
	}

	@Override
	public OperationLog save(OperationLog entity) {
		return operationLogRepository.save(entity);
	}

}
