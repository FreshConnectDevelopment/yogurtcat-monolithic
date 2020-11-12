package org.yogurtcat.server.modules.monitor.operationlog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLog;

/**
 * 操作日志持久化操作
 * @author heaven
 *
 */
public interface OperationLogRepository extends Repository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {

	/**
	 * 分页查询操作日志列表
	 * @param pageable
	 * @param username
	 * @return 日志列表
	 */
	public Page<OperationLog> findAllByUsername(Pageable pageable, String username);
	
	/**
	 * 新增操作日志
	 * @param entity
	 * @return 日志
	 */
	public OperationLog save(OperationLog entity);

	/**
	 * 分页查询操作日志列表
	 * @param pageable
	 */
	public void findAll(Pageable pageable);
}
