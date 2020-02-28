package org.yogurtcat.server.modules.resourcemanage.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.modules.resourcemanage.dao.ResourceRepository;
import org.yogurtcat.server.modules.resourcemanage.dao.ResourceSpec;
import org.yogurtcat.server.modules.resourcemanage.domain.Resource;

/**
 * 资源serviceImpl
 * @author s
 *
 */
@Service
public class ResourceServiceImpl implements ResourceService {
	
	@Autowired
	private ResourceRepository resourceRepository;

	@Override
	public Page<Resource> list(Optional<Resource> condition, Pageable pageable) {
		Page<Resource> page = resourceRepository.findAll(ResourceSpec.builder().condition(condition).build(), pageable);
		return page;
	}

	@Override
	public Resource save(Resource data) {
		return resourceRepository.save(data);
	}

	@Override
	public void delete(Resource data) {
		resourceRepository.delete(data);
	}

	@Override
	public Resource findById(Long id) {
		return resourceRepository.findById(id);
	}

}
