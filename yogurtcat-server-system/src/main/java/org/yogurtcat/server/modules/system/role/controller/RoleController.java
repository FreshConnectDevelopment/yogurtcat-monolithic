package org.yogurtcat.server.modules.system.role.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yogurtcat.server.common.constant.Module;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.monitor.operationlog.annotation.OperationLog;
import org.yogurtcat.server.modules.system.role.domain.Role;
import org.yogurtcat.server.modules.system.role.service.RoleService;

import com.google.common.collect.Lists;

/**
 * 登录认证
 * 
 * @author heaven
 *
 */
@RestController
@RequestMapping("role")
public class RoleController {

	@Autowired
	private RoleService service;

	/**
	 * 查询项目列表
	 * 
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list(Role condition, @RequestParam(defaultValue="20") Integer limit, 
			@RequestParam(defaultValue="1") Integer page) throws BusinessException {
		Page<Role> list = service.list(Optional.ofNullable(condition), PageRequest.of(page - 1, limit));
		if (Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		} else {
			return ResponseResult.builder().data(Lists.newArrayList()).build();
		}
	}

	/**
	 * 菜单下拉列表
	 * 
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@GetMapping(path = "optionlist")
	public ResponseResult optionlist() throws BusinessException {
		List<Map<String, Object>> list = service.optionlist();
		if (Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		} else {
			return ResponseResult.builder().data(Lists.newArrayList()).build();
		}
	}

	@GetMapping(value = "{id}")
	public ResponseResult get(@PathVariable Long id) throws BusinessException {
		Role result = service.findById(id);
		return ResponseResult.builder().data(result).build();
	}
    
	@OperationLog(logCode = "saveRole", module = Module.新增角色)
	@PostMapping(path = "create", consumes = "application/json")
	public ResponseResult create(@RequestBody Role data) throws BusinessException {
		Role oldRole = service.findByName(data.getName());
		if (oldRole != null) {
			throw BusinessException.builder().code(ResultCode.ROLE_EXITS_ERROR.getCode())
					.message(ResultCode.ROLE_EXITS_ERROR.getMessage()).build();
		}
		Role role = service.save(data);
		return ResponseResult.builder().data(role).build();
	}

	@OperationLog(logCode = "role", module = Module.修改角色)
	@PostMapping(path = "update", consumes = "application/json")
	public ResponseResult update(@RequestBody Role data) throws BusinessException {
		Role oldRole = service.findByName(data.getName());
		if (oldRole != null && !oldRole.getId().equals(data.getId())) {
			throw BusinessException.builder().code(ResultCode.ROLE_EXITS_ERROR.getCode())
					.message(ResultCode.ROLE_EXITS_ERROR.getMessage()).build();
		}
		Role role = service.save(data);
		return ResponseResult.builder().data(role).build();
	}

	@OperationLog(logCode = "deleteRole", module = Module.删除角色)
	@DeleteMapping(value = "{id}")
	public ResponseResult delete(@PathVariable Long id) throws BusinessException {
		try {
			service.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw BusinessException.builder().code(ResultCode.ROLE_ERROR.getCode()).message(ResultCode.ROLE_ERROR.getMessage()).build();
		}
		return ResponseResult.builder().build();
	}

	@PutMapping(path = "permission", consumes = "application/json")
	public ResponseResult updatePermission(@RequestBody Role data) throws BusinessException {
		return ResponseResult.builder().data(service.savePermission(data)).build();
	}

	@PutMapping(path = "menu", consumes = "application/json")
	public ResponseResult updateMenu(@RequestBody Role data) throws BusinessException {
		return ResponseResult.builder().data(service.saveMenu(data)).build();
	}

}
