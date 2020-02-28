package org.yogurtcat.server.modules.monitor.operationlog.advice;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yogurtcat.server.common.constant.Module;
import org.yogurtcat.server.common.utils.StringUtil;
import org.yogurtcat.server.commons.response.ResponseResult;
import org.yogurtcat.server.modules.monitor.operationlog.annotation.OperationLog;
import org.yogurtcat.server.modules.monitor.operationlog.service.OperationLogService;

/**
 * 操作日志切面
 * 
 * @author heaven
 *
 */
@Aspect
@Component
public class OperationLogAdvice {

	@Autowired
	private OperationLogService operationLogService;

	@Around(value = "(execution(public * *(..))) && @annotation(operationLog)", argNames = "joinPoint, operationLog")
	public Object process(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		String ip = StringUtil.getIp(request);
		ResponseResult retVal = (ResponseResult) joinPoint.proceed();

		Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {

			UserDetails userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			String username = userDetails.getUsername();

			String logCode = operationLog.logCode();
			Module module = operationLog.module();

			org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLog entity = org.yogurtcat.server.modules.monitor.operationlog.domain.OperationLog
					.builder().arg((Serializable) joinPoint.getArgs()[0]).ip(ip).logCode(logCode).module(module)
					.username(username).result(retVal.getMessage()).build();
			operationLogService.save(entity);
		}
		return retVal;
	}
}
