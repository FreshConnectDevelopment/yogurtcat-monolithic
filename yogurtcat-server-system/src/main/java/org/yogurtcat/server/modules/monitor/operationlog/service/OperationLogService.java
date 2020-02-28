package org.yogurtcat.server.modules.monitor.operationlog.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLog;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLogVo;

/**
 * 操作日志
 * @author heaven
 *
 */
public interface OperationLogService {

	/**
	 * 分页查询操作日志列表
	 * @param condition
	 * @param pageable
	 * @return
	 */
	public Page<OperationLogVo> findAll(Optional<OperationLogVo> condition, Pageable pageable);
	
	/**
	 * 新增操作日志
	 * @param entity
	 * @return
	 */
	public OperationLog save(OperationLog entity);


}
