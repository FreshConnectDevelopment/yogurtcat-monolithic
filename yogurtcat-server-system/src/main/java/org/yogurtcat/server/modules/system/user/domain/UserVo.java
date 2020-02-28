package org.yogurtcat.server.modules.system.user.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.yogurtcat.server.common.constant.UserStatus;
import org.yogurtcat.server.modules.system.menu.domain.SidebarMenuVo;
import org.yogurtcat.server.modules.system.role.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 * @author heaven
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo  implements UserDetails  {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 登录用户id
	 */
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
	 * 选择的角色列表
	 */
	private List<Long> roleIds;
	
	/**
	 * 角色信息
	 */
	private List<Role> roles;
	
	/**
	 * 菜单信息
	 */
	List<SidebarMenuVo> menus;
	
	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 登录凭证
	 */
	private String token;

	/**
	 * 状态
	 */
	//@NotNull(message="用户状态不能为空")
	@Enumerated(EnumType.ORDINAL)
    private UserStatus status;

	/**
	 * 注册时间
	 */
    private Date registrationTime;

	/**
	 * 头像
	 */
	private String avatar;
	
	/**
	 * 授权列表
	 */
	private Set<GrantedAuthority> authorities;
	
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
}
