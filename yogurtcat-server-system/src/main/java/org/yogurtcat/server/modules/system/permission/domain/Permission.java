package org.yogurtcat.server.modules.system.permission.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.yogurtcat.server.modules.system.role.domain.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色信息
 * 
 * @author heaven
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	/**
	 * 权限名
	 */
	private String name;

	/**
	 * 权限别名
	 */
	private String alias;

	/**
	 * 创建日期
	 */
	@CreationTimestamp
	private Date createTime;

	/**
	 * 父主键ID
	 */
	private Long pid;

	/**
	 * 角色信息
	 */
	@JsonIgnore
	@ManyToMany(mappedBy = "permissions")
	private Set<Role> roles;

	@Override
	public String getAuthority() {
		return this.getName();
	}

}
