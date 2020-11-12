package org.yogurtcat.server.modules.storage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.yogurtcat.server.common.storage.domain.Storage;

/**
 * 存储配置dao
 * @author s
 *
 */
public interface StorageRepository extends Repository<Storage, Long>, JpaSpecificationExecutor<Storage> {

	/**
	 * 新增或修改
	 * @param storage
	 * @return 存储配置
	 */
	Storage save(Storage storage);

	/**
	 * 删除
	 * @param storage
	 */
	void delete(Storage storage);

	/**
	 * 根据id查询
	 * @param id
	 * @return 存储配置
	 */
	Storage findById(Long id);
	
	/**
	 * 根据业务类型查询单个
	 * @param businessType
	 * @return 存储配置
	 */
	Storage findTopByBusinessTypeContaining(String businessType);

	/**
	 * id正序查询列表
	 * @return 存储配置
	 */
	List<Storage> findByOrderById();
}
