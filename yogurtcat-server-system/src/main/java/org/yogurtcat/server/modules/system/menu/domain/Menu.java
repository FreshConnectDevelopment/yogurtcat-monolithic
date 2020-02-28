package org.yogurtcat.server.modules.system.menu.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.yogurtcat.server.modules.system.role.domain.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 菜单信息
 * @author heaven
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 菜单名
	 */
	private String name;
	
	/**
	 * 路由路径
	 */
	private String path;
	
	/**
	 * 组件路径
	 */
	private String component;
	
	/**
	 * 图标样式名
	 */
	private String icon;
	
	/**
	 * 上级目录标识
	 */
	private Long pid;
	
	/**
	 * 菜单排序
	 */
	private Long sort;
	
	/**
	 * 创建日期
	 */
	private Long createTime;
	
	/**
	 * 角色信息
	 */
    @ManyToMany(mappedBy = "menus")
    @JsonIgnore
    private Set<Role> roles;
	
}
