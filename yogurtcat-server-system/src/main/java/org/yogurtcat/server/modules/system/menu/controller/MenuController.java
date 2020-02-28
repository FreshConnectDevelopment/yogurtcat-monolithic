package org.yogurtcat.server.modules.system.menu.controller;

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
import org.yogurtcat.server.modules.system.menu.domain.Menu;
import org.yogurtcat.server.modules.system.menu.domain.MenuVo;
import org.yogurtcat.server.modules.system.menu.service.MenuService;

import com.google.common.collect.Lists;

/**
 * 登录认证
 * 
 * @author heaven
 *
 */
@RestController
@RequestMapping("menu")
public class MenuController {

	@Autowired
	private MenuService service;

	/**
	 * 查询项目列表
	 * 
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list(MenuVo condition) throws BusinessException {
		List<MenuVo> list = service.list(Optional.ofNullable(condition));
		if (Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		} else {
			return ResponseResult.builder().data(Lists.newArrayList()).build();
		}
	}
	
	/**
	 * 菜单下拉列表
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
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode()).message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}

	@OperationLog(logCode = "saveMenu", module = Module.新增菜单)
	@PostMapping(path = "create", consumes = "application/json")
	public ResponseResult create(@RequestBody MenuVo data) throws BusinessException {
		Menu project = service.save(data);
		return ResponseResult.builder().data(project).build();
	}

	@OperationLog(logCode = "menu", module = Module.修改菜单)
	@PostMapping(path = "update", consumes = "application/json")
	public ResponseResult update(@RequestBody MenuVo data) throws BusinessException {
		Menu project = service.save(data);
		return ResponseResult.builder().data(project).build();
	}

	@OperationLog(logCode = "deleteMenu", module = Module.删除菜单)
    @DeleteMapping(value = "{id}")
	public ResponseResult delete(@PathVariable Long id) throws BusinessException {
		try {
			service.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw BusinessException.builder().code(ResultCode.MENU_ERROR.getCode()).message(ResultCode.MENU_ERROR.getMessage()).build();
		}
		return ResponseResult.builder().build();
	}

}
