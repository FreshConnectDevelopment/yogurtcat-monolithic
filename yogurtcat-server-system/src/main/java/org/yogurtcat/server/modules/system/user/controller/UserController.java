package org.yogurtcat.server.modules.system.user.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yogurtcat.server.common.constant.Module;
import org.yogurtcat.server.common.jwt.JwtService;
import org.yogurtcat.server.common.security.SecurityContextHolderHelper;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.monitor.operationlog.annotation.OperationLog;
import org.yogurtcat.server.modules.system.user.domain.UserVo;
import org.yogurtcat.server.modules.system.user.service.UserService;

/**
 * 登录认证
 * 
 * @author heaven
 *
 */
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password8
	 * @return 账号信息
	 * @throws BusinessException
	 */
	@ResponseBody
	@PostMapping(path = "login", consumes = "application/json")
	public ResponseResult login(@RequestBody UserVo user) throws BusinessException {
		String username = user.getUsername();
		UserVo loginUser = userService.findUser(username, user.getPassword());
		if (Optional.ofNullable(loginUser).isPresent()) {
			String tocken = jwtService.generateToken(username);
			loginUser.setToken(tocken);
			return ResponseResult.builder().data(loginUser).build();
		} else {
			throw BusinessException.builder().code(ResultCode.AUTH_FAIL.getCode())
					.message(ResultCode.AUTH_FAIL.getMessage()).build();
		}
	}

	@ResponseBody
	@PostMapping(path = "logout")
	public ResponseResult logout() throws BusinessException {
		return ResponseResult.builder().build();
	}

	@ResponseBody
	@GetMapping(path = "info")
	public ResponseResult info() throws BusinessException {
		String username = SecurityContextHolderHelper.getusername();
		UserVo loginUser = userService.findUserByUsername(username);
		if (Optional.ofNullable(loginUser).isPresent()) {
			return ResponseResult.builder().data(loginUser).build();
		} else {
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode())
					.message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}

	/**
	 * 查询项目列表
	 * 
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseResult list(UserVo condition, @RequestParam(defaultValue="20") Integer limit, 
			@RequestParam(defaultValue="1") Integer page) throws BusinessException {
		Page<UserVo> list = userService.list(Optional.ofNullable(condition), PageRequest.of(page - 1, limit));
		if (Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		} else {
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode())
					.message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}

	@OperationLog(logCode = "saveUser", module = Module.新增管理用户)
	@PostMapping(path = "create", consumes = "application/json")
	public ResponseResult create(@Valid @RequestBody UserVo data) throws BusinessException {
		UserVo oldUser = userService.findUserByUsername(data.getUsername());
		if (oldUser != null) {
			throw BusinessException.builder().code(ResultCode.NAME_EXITS_ERROR.getCode())
					.message(ResultCode.NAME_EXITS_ERROR.getMessage()).build();
		}
		UserVo project = userService.save(data);
		return ResponseResult.builder().data(project).build();
	}

	@OperationLog(logCode = "user", module = Module.修改管理用户)
	@PostMapping(path = "update", consumes = "application/json")
	public ResponseResult update(@RequestBody UserVo data) throws BusinessException {
		UserVo oldUser = userService.findUserByUsername(data.getUsername());
		if (oldUser != null && !oldUser.getId().equals(data.getId())) {
			throw BusinessException.builder().code(ResultCode.NAME_EXITS_ERROR.getCode())
					.message(ResultCode.NAME_EXITS_ERROR.getMessage()).build();
		}
		UserVo project = userService.save(data);
		return ResponseResult.builder().data(project).build();
	}

	@OperationLog(logCode = "user", module = Module.删除管理用户)
	@PostMapping(path = "delete", consumes = "application/json")
	public ResponseResult delete(@RequestBody UserVo data) throws BusinessException {
		userService.delete(data);
		return ResponseResult.builder().build();
	}

}
