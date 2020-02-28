package org.yogurtcat.server.modules.storage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.common.storage.domain.Storage;
import org.yogurtcat.server.modules.storage.dao.StorageRepository;
import org.yogurtcat.server.modules.storage.dao.StorageSpec;
import org.yogurtcat.server.modules.storage.domain.StorageVO;

/**
 * 查询配置serviceImpl
 * @author s
 *
 */
@Service
public class StorageServiceImpl implements StorageService {
	
	@Autowired
	private StorageRepository storageRepository;

	@Override
	public Page<Storage> list(Optional<StorageVO> condition, Pageable pageable) {
		Page<Storage> page = storageRepository.findAll(StorageSpec.builder().condition(condition).build(), pageable);
		return page;
	}

	@Override
	public Storage save(StorageVO data) {
		Storage target = new Storage();
		BeanUtils.copyProperties(data, target);
		target = storageRepository.save(target);
		return target;
	}

	@Override
	public void delete(StorageVO data) {
		Storage target = new Storage();
		BeanUtils.copyProperties(data, target);
		storageRepository.delete(target);
	}

	@Override
	public Storage findById(Long id) {
		return storageRepository.findById(id);
	}

	@Override
	public Storage findByBusinessType(String businessType) {
		return storageRepository.findTopByBusinessTypeContaining(businessType);
	}

	@Override
	public List<Storage> findAll() {
		return storageRepository.findByOrderById();
	}

}
