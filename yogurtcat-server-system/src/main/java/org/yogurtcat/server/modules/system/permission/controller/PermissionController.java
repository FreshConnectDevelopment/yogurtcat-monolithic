package org.yogurtcat.server.modules.system.permission.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yogurtcat.server.common.constant.Module;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.monitor.operationlog.annotation.OperationLog;
import org.yogurtcat.server.modules.system.permission.domain.Permission;
import org.yogurtcat.server.modules.system.permission.domain.PermissionVo;
import org.yogurtcat.server.modules.system.permission.service.PermissionService;

import com.google.common.collect.Lists;

/**
 * 登录认证
 * @author heaven
 *
 */
@RestController
@RequestMapping("permission")
public class PermissionController {

	@Autowired
	private PermissionService service;

	/**
	 * 查询权限列表
	 * 
	 * @param condition
	 * @return 权限列表
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list(PermissionVo condition) throws BusinessException {
		List<PermissionVo> list = service.list(Optional.ofNullable(condition));
		if (Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		} else {
			return ResponseResult.builder().data(Lists.newArrayList()).build();
		}
	}
	
	/**
	 * 菜单下拉列表
	 * @return 菜单下拉列表
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

	@OperationLog(logCode = "savePermission", module = Module.新增权限)
	@PostMapping(path = "create", consumes = "application/json")
	public ResponseResult create(@RequestBody PermissionVo data) throws BusinessException {
		Permission project = service.save(data);
		return ResponseResult.builder().data(project).build();
	}

	@OperationLog(logCode = "permission", module = Module.修改权限)
	@PostMapping(path = "update", consumes = "application/json")
	public ResponseResult update(@RequestBody PermissionVo data) throws BusinessException {
		Permission project = service.save(data);
		return ResponseResult.builder().data(project).build();
	}

	@OperationLog(logCode = "deletePermission", module = Module.删除权限)
    @DeleteMapping(value = "{id}")
	public ResponseResult delete(@PathVariable Long id) throws BusinessException {
		try {
			service.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw BusinessException.builder().code(ResultCode.PERMISSION_ERROR.getCode()).message(ResultCode.PERMISSION_ERROR.getMessage()).build();
		}
		return ResponseResult.builder().build();
	}

}
