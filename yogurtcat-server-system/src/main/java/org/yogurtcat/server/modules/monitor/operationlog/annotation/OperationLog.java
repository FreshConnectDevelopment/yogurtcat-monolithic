package org.yogurtcat.server.modules.monitor.operationlog.annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.yogurtcat.server.common.constant.Module;

/**
 * 管理员操作日志切点注解
 * @author s
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface OperationLog {

	/**
	 * 日志id
	 * @see 
	 * @return
	 */
	String logCode();
	
	/**
	 * 功能模块
	 * @return
	 */
	Module module();
	
	

}
