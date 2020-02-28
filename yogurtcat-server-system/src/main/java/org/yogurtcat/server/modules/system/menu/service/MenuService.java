package org.yogurtcat.server.modules.system.menu.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.yogurtcat.server.modules.system.menu.domain.Menu;
import org.yogurtcat.server.modules.system.menu.domain.MenuVo;

/**
 * 菜单业务接口
 * @author heaven
 *
 */
public interface MenuService {
	
	/**
	 * 条件查询菜单
	 * @param condition
	 * @return
	 */
	List<MenuVo> list(Optional<MenuVo> condition);

	/**
	 * 新增或修改
	 * @param data
	 * @return
	 */
	Menu save(MenuVo data);

	/**
	 * 菜单下拉列表
	 * @return
	 */
	List<Map<String, Object>> optionlist();

	/**
	 * 删除
	 * @param id
	 */
	void deleteById(Long id);
	
}
