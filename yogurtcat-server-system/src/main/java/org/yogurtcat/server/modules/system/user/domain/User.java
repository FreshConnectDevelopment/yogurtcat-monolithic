package org.yogurtcat.server.modules.system.user.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.yogurtcat.server.common.constant.UserStatus;
import org.yogurtcat.server.modules.system.role.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 管理员实体
 * @author s
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 登录用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 角色信息
	 */
    @ManyToMany
    @JoinTable(name = "roles_users", joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")})
    private Set<Role> roles;

	/**
	 * 登录凭证
	 */
	private String token;

	/**
	 * 头像
	 */
	private String avatar; 
	
	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 状态
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
    private UserStatus status;
	
	/**
	 * 账号未过期
	 */
	private boolean accountNonExpired;
	
	/**
	 * 账号未锁定
	 */
	private boolean accountNonLocked;
	
	/**
	 * 凭证未过期
	 */
	private boolean credentialsNonExpired;
	
	/**
	 * 账号启用
	 */
	private boolean enabled;

	/**
	 * 注册时间
	 */
    @CreationTimestamp
    private Date registrationTime;
}
