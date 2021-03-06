package org.yogurtcat.server.modules.resourcemanage.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yogurtcat.server.modules.resourcemanage.domain.Resource;

/**
 * 资源service
 * @author s
 *
 */
public interface ResourceService {
	
	/**
	 * 条件分页查询
	 * @param condition
	 * @param pageable
	 * @return 资源信息列表
	 */
	Page<Resource> list(Optional<Resource> condition, Pageable pageable);

	/**
	 * 新增或修改
	 * @param data
	 * @return 资源信息
	 */
	Resource save(Resource data);

	/**
	 * 删除
	 * @param data
	 */
	void delete(Resource data);

	/**
	 * id查询
	 * @param id
	 * @return 资源信息
	 */
	Resource findById(Long id);
}
