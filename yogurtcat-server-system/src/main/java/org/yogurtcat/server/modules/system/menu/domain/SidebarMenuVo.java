package org.yogurtcat.server.modules.system.menu.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单信息
 * @author heaven
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SidebarMenuVo{
	
	/**
	 * 主键id
	 */
	@JsonIgnore
	private Long id;
	
	/**
	 * 上级目录标识
	 */
	@JsonIgnore
	private Long pid;
	
	/**
	 * 菜单排序
	 */
	private Long sort;
	
	/**
	 * 名称
	 * tag-view菜单名
	 */
	private String name;
	
	/**
	 * 请求路径
	 */
	private String path;
	
	/**
	 * 组件路径
	 */
	private String component = "Layout";
	
	/**
	 * 元数据
	 * 放置菜单标题,菜单图标
	 */
	private MenuMetaVo meta;
	
	/**
	 * 子菜单
	 */
	private List<SidebarMenuVo> children;
}
