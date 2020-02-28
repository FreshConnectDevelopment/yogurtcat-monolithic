package org.yogurtcat.server.modules.system.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单信息
 * @author heaven
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuMetaVo {
	
	/**
	 * 菜单名
	 */
	private String title;
	
	/**
	 * 菜单图标
	 */
	private String icon;
}
