package org.yogurtcat.server.modules.resourcemanage.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.modules.resourcemanage.domain.Resource;

/**
 * 资源dao
 * @author s
 *
 */
public interface ResourceRepository extends Repository<Resource, Long>, JpaSpecificationExecutor<Resource> {

	/**
	 * 新增或修改
	 * @param resource
	 * @return 资源信息
	 */
	Resource save(Resource resource);

	/**
	 * 删除
	 * @param resource
	 */
	void delete(Resource resource);

	/**
	 * 根据id查找
	 * @param id
	 * @return 资源信息
	 */
	Resource findById(Long id);
	
}
