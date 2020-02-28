package org.yogurtcat.server.modules.module.domain;

import org.yogurtcat.server.commons.constant.ModuleLevel;

import lombok.Builder;
import lombok.Data;

/**
 * 模块信息
 * @author heaven
 *
 */
@Data
@Builder
public class Module {
	
	/**
	 * 模块id
	 */
	private Long id;
	
	/**
	 * 父模块id
	 */
	private Long parentId;
	
	/**
	 * 模块名
	 */
	private String name;

	/**
	 * 菜单名
	 */
	private String menuName;
	
	/**
	 * 模块级别
	 */
	private ModuleLevel level;

}
