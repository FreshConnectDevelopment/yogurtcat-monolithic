package org.yogurtcat.server.modules.system.role.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.yogurtcat.server.modules.system.role.domain.Role;

/**
 * 角色业务接口
 * @author heaven
 *
 */
public interface RoleService {
	
	/**
	 * 分页条件查询
	 * @param condition
	 * @param pageRequest
	 * @return
	 */
	Page<Role> list(Optional<Role> condition, PageRequest pageRequest);

	/**
	 * 新增或保存
	 * @param data
	 * @return
	 */
	Role save(Role data);

	/**
	 * 删除
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * id查找
	 * @param id
	 * @return
	 */
	Role findById(Long id);

	/**
	 * 保存或修改角色权限
	 * @param data
	 * @return
	 */
	Role savePermission(Role data);

	/**
	 * 保存或修改角色菜单
	 * @param data
	 * @return
	 */
	Role saveMenu(Role data);

	/**
	 * 角色下拉列表
	 * @return
	 */
	List<Map<String, Object>> optionlist();

	/**
	 * name查询权限
	 * @param name
	 * @return
	 */
	Role findByName(String name);
}
