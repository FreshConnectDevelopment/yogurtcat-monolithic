package org.yogurtcat.server.modules.system.permission.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.modules.system.permission.domain.Permission;

/**
 * 权限dao
 * @author s
 *
 */
public interface PermissionRepository extends Repository<Permission, Long>, JpaSpecificationExecutor<Permission> {

	/**
	 * 新增或修改
	 * @param value
	 */
	void save(Permission value);

	/**
	 * 根据父id查询子权限
	 * @param pid
	 * @return
	 */
	List<Permission> findAllByPid(long pid);

	/**
	 * 删除
	 * @param id
	 */
	void deleteById(Long id);
}
