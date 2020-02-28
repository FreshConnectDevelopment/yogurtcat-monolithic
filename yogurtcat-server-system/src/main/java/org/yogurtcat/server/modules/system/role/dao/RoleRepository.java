package org.yogurtcat.server.modules.system.role.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.modules.system.role.domain.Role;

/**
 * 角色dao
 * @author s
 *
 */
public interface RoleRepository extends Repository<Role, Long>, JpaSpecificationExecutor<Role> {

	/**
	 * 新增或修改
	 * @param value
	 */
	void save(Role value);

	/**
	 * 查询全部
	 * @return
	 */
	List<Role> findAll();

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	Role findById(Long id);

	/**
	 * 删除
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 根据name查询
	 * @param name
	 * @return
	 */
	Role findByName(String name);
}
