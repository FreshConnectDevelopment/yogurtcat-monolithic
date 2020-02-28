package org.yogurtcat.server.modules.system.menu.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yogurtcat.server.commons.mapper.MenuMapper;
import org.yogurtcat.server.modules.system.menu.dao.MenuRepository;
import org.yogurtcat.server.modules.system.menu.dao.MenuSpec;
import org.yogurtcat.server.modules.system.menu.domain.Menu;
import org.yogurtcat.server.modules.system.menu.domain.MenuVo;

/**
 * 用户业务类
 * 
 * @author heaven
 *
 */
@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository repository;

	@Override
	public List<MenuVo> list(Optional<MenuVo> condition) {
		List<Menu> menuList = repository.findAll(MenuSpec.builder().condition(condition).build(),
				Sort.by(Direction.ASC, "sort"));
		if (!CollectionUtils.isEmpty(menuList)) {
			return buildMenuTree(menuList);
		}
		return null;
	}

	private List<MenuVo> buildMenuTree(List<Menu> menus) {
		Set<Long> set = menus.stream().map(value -> value.getId()).collect(Collectors.toSet());
		List<MenuVo> menuVoList = menus.stream().map(value -> MenuMapper.INSTANCE.toMenuVo(value))
				.collect(Collectors.toList());
		menuVoList.stream().forEach(current -> {
			current.setChildren(menuVoList.stream().filter(value -> value.getPid().equals(current.getId()))
					.collect(Collectors.toList()));
		});
		return menuVoList.stream().filter(value -> !set.contains(value.getPid())).collect(Collectors.toList());
	}

	@Override
	public Menu save(MenuVo data) {
		Menu target = new Menu();
		BeanUtils.copyProperties(data, target);
		repository.save(target);
		return target;
	}

	@Override
	public void deleteById(Long id) {
		List<Menu> childMenuList = repository.findAllByPid(id);
		if (!CollectionUtils.isEmpty(childMenuList)) {
			deleteChildMenus(childMenuList);
		}
		repository.deleteById(id);
	}

	private void deleteChildMenus(List<Menu> menus) {
		menus.stream().forEach(menu -> {
			List<Menu> menuList = repository.findAllByPid(menu.getId());
			if (!CollectionUtils.isEmpty(menuList)) {
				deleteChildMenus(menuList);
			}
			repository.deleteById(menu.getId());
		});
	}

	@Override
	public List<Map<String, Object>> optionlist() {
		List<Menu> menuList = repository.findAllByPid(0L);
		if (!CollectionUtils.isEmpty(menuList)) {
			return getChildMenus(menuList);
		}
		return null;
	}

	private List<Map<String, Object>> getChildMenus(List<Menu> menus) {
		List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
		menus.stream().forEach(menu -> {
			Map<String, Object> menuMap = new HashMap<String, Object>(16);
			menuMap.put("id", menu.getId());
			menuMap.put("label", menu.getName());
			List<Menu> childMenuList = repository.findAllByPid(menu.getId());
			if (!CollectionUtils.isEmpty(childMenuList)) {
				menuMap.put("children", getChildMenus(childMenuList));
			}
			result.add(menuMap);
		});
		return result;
	}

}