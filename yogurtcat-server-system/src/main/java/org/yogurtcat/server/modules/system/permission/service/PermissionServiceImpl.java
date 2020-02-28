package org.yogurtcat.server.modules.system.permission.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yogurtcat.server.modules.system.permission.dao.PermissionRepository;
import org.yogurtcat.server.modules.system.permission.dao.PermissionSpec;
import org.yogurtcat.server.modules.system.permission.domain.Permission;
import org.yogurtcat.server.modules.system.permission.domain.PermissionVo;

import com.google.common.collect.Lists;

/**
 * 用户业务类
 * 
 * @author heaven
 *
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository repository;

	@Override
	public List<PermissionVo> list(Optional<PermissionVo> condition) {
		List<Permission> menuList = repository.findAll(PermissionSpec.builder().condition(condition).build(),
				Sort.by(Direction.ASC, "pid"));
		if (menuList.size() > 0) {
			return buildPermissionTree(menuList);
		}
		return null;
	}

	private List<PermissionVo> buildPermissionTree(List<Permission> menus) {
		Map<Long, PermissionVo> menuMap = new HashMap<Long, PermissionVo>(16);
		menus.stream().forEach(menu -> {
			PermissionVo menuVo = new PermissionVo();
			BeanUtils.copyProperties(menu, menuVo);
			Long id = menuVo.getId();
			Long pid = menuVo.getPid();
			menuMap.put(id, menuVo);
			PermissionVo pPermissionVo = menuMap.get(pid);
			if (pPermissionVo != null) {
				List<PermissionVo> children = pPermissionVo.getChildren();
				if (children == null) {
					pPermissionVo.setChildren(Lists.newArrayList(menuVo));
				} else {
					pPermissionVo.getChildren().add(menuVo);
				}
			}
		});
		return Lists.newArrayList(
				menuMap.values().stream().filter(value -> value.getPid().equals(1L)).collect(Collectors.toList()));
	}

	@Override
	public Permission save(PermissionVo data) {
		Permission target = new Permission();
		BeanUtils.copyProperties(data, target);
		repository.save(target);
		return target;
	}

	@Override
	public void deleteById(Long id) {
		List<Permission> childPermissionList = repository.findAllByPid(id);
		if (!CollectionUtils.isEmpty(childPermissionList)) {
			deleteChildPermissions(childPermissionList);
		}
		repository.deleteById(id);
	}

	private void deleteChildPermissions(List<Permission> menus) {
		menus.stream().forEach(menu -> {
			List<Permission> menuList = repository.findAllByPid(menu.getId());
			if (!CollectionUtils.isEmpty(menuList)) {
				deleteChildPermissions(menuList);
			}
			repository.deleteById(menu.getId());
		});
	}

	@Override
	public List<Map<String, Object>> optionlist() {
		List<Permission> menuList = repository.findAllByPid(0L);
		if (!CollectionUtils.isEmpty(menuList)) {
			return getChildPermissions(menuList);
		}
		return null;
	}

	private List<Map<String, Object>> getChildPermissions(List<Permission> menus) {
		List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
		menus.stream().forEach(menu -> {
			Map<String, Object> menuMap = new HashMap<String, Object>(16);
			menuMap.put("id", menu.getId());
			menuMap.put("label", menu.getAlias());
			List<Permission> childPermissionList = repository.findAllByPid(menu.getId());
			if (!CollectionUtils.isEmpty(childPermissionList)) {
				menuMap.put("children", getChildPermissions(childPermissionList));
			}
			result.add(menuMap);
		});
		return result;
	}

}
