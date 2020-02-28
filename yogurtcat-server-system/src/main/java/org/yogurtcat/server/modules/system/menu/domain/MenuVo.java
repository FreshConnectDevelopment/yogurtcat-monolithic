package org.yogurtcat.server.modules.system.menu.domain;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单信息
 * @author heaven
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
public class MenuVo extends Menu {

	private static final long serialVersionUID = 1L;
	
	private List<MenuVo> children;
}
