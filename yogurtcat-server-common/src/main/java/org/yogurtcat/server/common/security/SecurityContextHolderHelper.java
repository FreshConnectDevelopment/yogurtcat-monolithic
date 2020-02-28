package org.yogurtcat.server.common.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * SecurityContextHolder辅助类
 * @author heaven
 *
 */
public class SecurityContextHolderHelper {

	/**
	 * spring security 认证用户名获取
	 * @return
	 */
	public static String getusername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return username;
	}

}
