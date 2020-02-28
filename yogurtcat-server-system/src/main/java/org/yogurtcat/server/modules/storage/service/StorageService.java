package org.yogurtcat.server.modules.storage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yogurtcat.server.common.storage.domain.Storage;
import org.yogurtcat.server.modules.storage.domain.StorageVO;

/**
 * 存储配置service
 * @author s
 *
 */
public interface StorageService {
	
	/**
	 * 条件分页查询
	 * @param condition
	 * @param pageable
	 * @return
	 */
	Page<Storage> list(Optional<StorageVO> condition, Pageable pageable);

	/**
	 * 新增或修改
	 * @param data
	 * @return
	 */
	Storage save(StorageVO data);

	/**
	 * 删除
	 * @param data
	 */
	void delete(StorageVO data);

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	Storage findById(Long id);
	
	/**
	 * 查询符合条件的上传方式
	 * @param businessType
	 * @return
	 */
	Storage findByBusinessType(String businessType);
	
	/**
	 * 查询所有
	 * @return
	 */
	List<Storage> findAll();
}
