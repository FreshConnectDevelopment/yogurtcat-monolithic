package org.yogurtcat.server.modules.monitor.operationlog.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLog;
import org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLogVo;
import org.yogurtcat.server.modules.monitor.operationlog.service.OperationLogService;

/**
 * 管理员操作日志接口
 * @author s
 *
 */
@RestController
@RequestMapping("operationLog")
public class OperationLogController {

	@Autowired
	private OperationLogService operationLogService;

	/**
	 * 操作日志列表
	 * 
	 * @param condition
	 * @return 操作日志
	 * @throws BusinessException
	 */
	@GetMapping(path = "list")
	public ResponseResult list(OperationLogVo condition, @RequestParam(defaultValue="20") Integer limit, 
			@RequestParam(defaultValue="1") Integer page) throws BusinessException {
		Page<OperationLogVo> list = operationLogService.findAll(Optional.ofNullable(condition), PageRequest.of(page - 1, limit));
		if (Optional.ofNullable(list).isPresent()) {
			return ResponseResult.builder().data(list).build();
		} else {
			throw BusinessException.builder().code(ResultCode.SYSTEM_ERROR.getCode())
					.message(ResultCode.SYSTEM_ERROR.getMessage()).build();
		}
	}

	@PostMapping(path = "create", consumes = "application/json")
	public ResponseResult create(@RequestBody OperationLog data) throws BusinessException {
		return ResponseResult.builder().data(operationLogService.save(data)).build();
	}

}
