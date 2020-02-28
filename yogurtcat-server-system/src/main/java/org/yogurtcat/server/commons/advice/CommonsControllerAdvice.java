package org.yogurtcat.server.commons.advice;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yogurtcat.server.commons.constant.ResultCode;
import org.yogurtcat.server.commons.exception.BusinessException;
import org.yogurtcat.server.commons.response.ResponseResult;

import lombok.extern.slf4j.Slf4j;

/**
 * controller切面
 * 
 * @author heaven
 *
 */
@Slf4j
@ControllerAdvice
public class CommonsControllerAdvice {

	@ResponseBody
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseResult handle(MethodArgumentNotValidException ex) {
		StringBuffer errorMessage = new StringBuffer();
		ex.getBindingResult().getAllErrors().stream().forEach(value->{
			errorMessage.append(getError(value));
		});
		return ResponseResult.builder().code(ResultCode.METHOD_ARGUMENT_NOT_VALID_ERROR.getCode()).message(errorMessage.toString()).build();
	}

	private String getError(ObjectError value) {
		return value.getDefaultMessage();
	}

	@ResponseBody
	@ExceptionHandler({ IllegalStateException.class })
	public ResponseResult handle(BusinessException ex) {
		return ResponseResult.builder().code(ex.getCode()).message(ex.getMessage()).build();
	}

	@ResponseBody
	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseResult handle(AccessDeniedException ex) {
		return ResponseResult.builder().code(ResultCode.ACCESS_DENIED_ERROR.getCode())
				.message(ResultCode.ACCESS_DENIED_ERROR.getMessage()).build();
	}

	@ResponseBody
	@ExceptionHandler({ Exception.class })
	public ResponseResult handle(Exception ex) {
		log.error(CommonsControllerAdvice.class.getCanonicalName(), "system error", ex);
		return ResponseResult.builder().code(ResultCode.SYSTEM_ERROR.getCode())
				.message(ResultCode.SYSTEM_ERROR.getMessage()).build();
	}
}
