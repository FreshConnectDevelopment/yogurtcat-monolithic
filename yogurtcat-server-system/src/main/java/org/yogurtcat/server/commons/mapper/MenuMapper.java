package org.yogurtcat.server.commons.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yogurtcat.server.modules.system.menu.domain.Menu;
import org.yogurtcat.server.modules.system.menu.domain.MenuVo;
import org.yogurtcat.server.modules.system.menu.domain.SidebarMenuVo;

/**
 * 菜单实体类映射
 * @author s
 *
 */
@Mapper
public interface MenuMapper {

	MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

	/**
	 * Menu to SidebarMenuVo
	 * @param menu
	 * @return 侧边栏菜单对象
	 */
	@Mapping(source = "name", target = "name")
	@Mapping(source = "name", target = "meta.title")
	@Mapping(source = "icon", target = "meta.icon")
	@Mapping(target = "component", source = "component", defaultValue = "Layout")
	SidebarMenuVo toSidebarMenuVo(Menu menu);

	/**
	 * Menu to MenuVo
	 * @param menu
	 * @return 菜单视图对象
	 */
	MenuVo toMenuVo(Menu menu);
}
