package org.yogurtcat.server.modules.system.role.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.yogurtcat.server.modules.system.menu.domain.Menu;
import org.yogurtcat.server.modules.system.permission.domain.Permission;
import org.yogurtcat.server.modules.system.user.domain.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色信息
 * @author heaven
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 角色名
	 */
	private String name;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 创建日期
	 */
    @CreationTimestamp
	private Date createTime;
	
	/**
	 * 关联用户信息
	 */
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
	private Set<User> users;
	
	/**
	 * 关联菜单信息
	 */
    @ManyToMany
    @JoinTable(name = "roles_menus", joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "menu_id",referencedColumnName = "id")})
	private Set<Menu> menus;
	
	/**
	 * 关联角色信息
	 */
    @ManyToMany
    @JoinTable(name = "roles_permissions", joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id",referencedColumnName = "id")})
	private Set<Permission> permissions;
	
	
}
