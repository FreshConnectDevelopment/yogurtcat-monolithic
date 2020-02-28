package org.yogurtcat.server.modules.system.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yogurtcat.server.commons.mapper.MenuMapper;
import org.yogurtcat.server.commons.mapper.UserMapper;
import org.yogurtcat.server.modules.system.menu.domain.SidebarMenuVo;
import org.yogurtcat.server.modules.system.role.domain.Role;
import org.yogurtcat.server.modules.system.user.dao.UserRepository;
import org.yogurtcat.server.modules.system.user.dao.UserSpec;
import org.yogurtcat.server.modules.system.user.domain.User;
import org.yogurtcat.server.modules.system.user.domain.UserVo;

import com.google.common.collect.Sets;

/**
 * 用户业务类
 * 
 * @author heaven
 *
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserVo findUser(String name, String password) {
		Optional<UserVo> condition = Optional.ofNullable(UserVo.builder().username(name).password(password).build());
		Optional<User> user = userRepository.findOne(UserSpec.builder().condition(condition).build());
		if (!user.isPresent()) {
			return null;
		}
		UserVo target = new UserVo();
		BeanUtils.copyProperties(user.get(), target);
		return target;
	}

	@Override
	public UserVo findUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if(user != null) {
			UserVo target = UserMapper.INSTANCE.toUserVo(user);
			target.setMenus(buildMenu(user));
			Set<GrantedAuthority> authorities = mapperGrantedAuthority(user);
			target.setAuthorities(authorities);
			return target;
		}
		return null;
	}

	private List<SidebarMenuVo> buildMenu(User user) {
		List<SidebarMenuVo> sidebarMenuSet = new ArrayList<SidebarMenuVo>();
		user.getRoles().stream().forEach(role ->{ role.getMenus().stream().forEach(menu ->{
				SidebarMenuVo sidebarMenuVo = MenuMapper.INSTANCE.toSidebarMenuVo(menu);
				sidebarMenuVo.setSort(menu.getSort());
				sidebarMenuSet.add(sidebarMenuVo);
			});
		});
		sidebarMenuSet.sort((a, b) -> Long.compare(a.getSort(), b.getSort()));
		return buildMenuTreeForSidebar(sidebarMenuSet.stream().collect(Collectors.toList()));
	}

	private List<SidebarMenuVo> buildMenuTreeForSidebar(List<SidebarMenuVo> menuVoList) {
		Set<Long> set = menuVoList.stream().map(value -> value.getId()).collect(Collectors.toSet());
		menuVoList.stream().forEach(current -> {
			current.setChildren(menuVoList.stream().filter(value -> value.getPid().equals(current.getId()))
					.collect(Collectors.toList()));
		});
		return menuVoList.stream().filter(value -> !set.contains(value.getPid())).collect(Collectors.toList());
	}

	@Override
	public Page<UserVo> list(Optional<UserVo> condition, Pageable pageable) {
		Page<User> users = userRepository.findAll(UserSpec.builder().condition(condition).build(), pageable);
		List<UserVo> content = users.getContent().stream().map(value -> mapperUserVo(value))
				.collect(Collectors.toList());
		return new PageImpl<UserVo>(content, users.getPageable(), users.getTotalElements());
	}

	private UserVo mapperUserVo(User value) {
		List<Long> roleIds = null;
		if (value.getRoles() != null) {
			roleIds = value.getRoles().stream().map(user -> user.getId()).collect(Collectors.toList());
		}
		return UserVo.builder().avatar(value.getAvatar()).email(value.getEmail()).enabled(value.isEnabled())
				.id(value.getId()).registrationTime(value.getRegistrationTime()).roleIds(roleIds)
				.status(value.getStatus()).username(value.getUsername()).build();
	}

	@Override
	public UserVo save(UserVo data) {
		User target = new User();
		BeanUtils.copyProperties(data, target);
		if (StringUtils.isEmpty(target.getPassword())) {
			User user = userRepository.findByUsername(data.getUsername());
			target.setPassword(user.getPassword());
		}
		Set<Role> roles = Sets.newHashSet();
		data.getRoleIds().stream().forEach(roleId -> roles.add(Role.builder().id(roleId).build()));
		target.setRoles(roles);
		User result = userRepository.save(target);
		return mapperUserVo(result);
	}

	@Override
	public void delete(UserVo data) {
		User target = new User();
		BeanUtils.copyProperties(data, target);
		userRepository.delete(target);
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		UserVo data = new UserVo();
		BeanUtils.copyProperties(user, data);
		Set<GrantedAuthority> authorities = mapperGrantedAuthority(user);
		data.setAuthorities(authorities);
		return data;
	}

	private Set<GrantedAuthority> mapperGrantedAuthority(User user) {
		Set<GrantedAuthority> permissions = Sets.newHashSet();
		user.getRoles().forEach(role -> {
			permissions.addAll(role.getPermissions());
		});
		return permissions;
	}
}
