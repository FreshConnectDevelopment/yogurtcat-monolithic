package org.yogurtcat.server.commons.constant;

import org.springframework.core.io.ClassPathResource;

import lombok.Getter;

/**
 * 代码类型
 * @author s
 *
 */
public enum CodeType {
	/**
	 * 前端代码
	 */
	前端代码(new ClassPathResource("/codetemplate/vue")), 
	/**
	 * 后端代码
	 */
	后端代码(new ClassPathResource("/codetemplate/server")), 
	/**
	 * 数据库启动代码
	 */
	数据库启动代码(new ClassPathResource("/codetemplate/sql"));
	
	/**
	 * 代码模块资源
	 */
	@Getter
	ClassPathResource template;
	
	CodeType(ClassPathResource template){
		this.template = template;
	}
}
