package org.yogurtcat.server.modules.system.permission.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.yogurtcat.server.modules.system.permission.domain.Permission;
import org.yogurtcat.server.modules.system.permission.domain.PermissionVo;

/**
 * 权限业务接口
 * @author heaven
 *
 */
public interface PermissionService {
	
	/**
	 * 查询权限列表
	 * @param condition
	 * @return
	 */
	List<PermissionVo> list(Optional<PermissionVo> condition);

	/**
	 * 新增或修改
	 * @param data
	 * @return
	 */
	Permission save(PermissionVo data);

	/**
	 * 权限下拉列表
	 * @return
	 */
	List<Map<String, Object>> optionlist();

	/**
	 * 删除
	 * @param id
	 */
	void deleteById(Long id);
	
}
