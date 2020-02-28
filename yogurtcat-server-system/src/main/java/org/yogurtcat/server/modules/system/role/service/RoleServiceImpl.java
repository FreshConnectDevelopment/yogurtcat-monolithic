package org.yogurtcat.server.modules.system.role.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.modules.system.role.dao.RoleRepository;
import org.yogurtcat.server.modules.system.role.dao.RoleSpec;
import org.yogurtcat.server.modules.system.role.domain.Role;

/**
 * 用户业务类
 * 
 * @author heaven
 *
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository repository;

	@Override
	public Page<Role> list(Optional<Role> condition, PageRequest pageRequest) {
		return repository.findAll(RoleSpec.builder().condition(condition).build(), pageRequest);
	}

	@Override
	public List<Map<String, Object>> optionlist() {
        List<Role> roleList = repository.findAll();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Role role : roleList) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("id",role.getId());
            map.put("label",role.getName());
            list.add(map);
        }
        return list;
	}

	@Override
	public Role save(Role data) {
		repository.save(data);
		return data;
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Role findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Role savePermission(Role data) {
		Role role = repository.findById(data.getId());
		role.setPermissions(data.getPermissions());
		repository.save(role);
		return role;
	}

	@Override
	public Role saveMenu(Role data) {
		Role role = repository.findById(data.getId());
		role.setMenus(data.getMenus());
		repository.save(role);
		return role;
	}

	@Override
	public Role findByName(String name) {
		return repository.findByName(name);
	}

}
