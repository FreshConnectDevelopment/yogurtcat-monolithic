package org.yogurtcat.server.commons.constant;

import lombok.Getter;

/**
 * 模板类型
 * @author s
 *
 */
public enum TemplateType {

	前端API代码模板("vue_api.ftl", "/api/", ".js", TemplateCreateType.仅生成文件, CodeType.前端代码),
	前端路由入口模板("router_index.ftl", "/router/index.js", "", TemplateCreateType.覆盖原文件, CodeType.前端代码),
	前端路由模块模板("router_module.ftl", "/router/modules/", ".js", TemplateCreateType.仅生成文件, CodeType.前端代码),
	前端视图模块模板("view_index.ftl", "/views/${module}/", "index.vue", TemplateCreateType.创建目录及文件, CodeType.前端代码),
	后端controller模块模板("controller.ftl", "/${module}/controller/", "Controller.java", TemplateCreateType.创建目录及文件, CodeType.后端代码),
	后端service接口模板("service.ftl", "/${module}/service/", "Service.java", TemplateCreateType.创建目录及文件, CodeType.后端代码),
	后端service实现模板("serviceImpl.ftl", "/${module}/service/", "ServiceImpl.java", TemplateCreateType.创建目录及文件, CodeType.后端代码),
	后端JPA实体模板("domain.ftl", "/${module}/domain/", ".java", TemplateCreateType.创建目录及文件, CodeType.后端代码),
	后端视图实体模板("domainVo.ftl", "/${module}/domain/", "Vo.java", TemplateCreateType.创建目录及文件, CodeType.后端代码),
	后端JPA仓库模板("repository.ftl", "/${module}/dao/", "Repository.java", TemplateCreateType.创建目录及文件, CodeType.后端代码),
	后端JPA条件模板("spec.ftl", "/${module}/dao/", "Spec.java", TemplateCreateType.创建目录及文件, CodeType.后端代码), 
	启动数据模板("importsql.ftl", "/source-generator/src/main/resources/import.sql", "", TemplateCreateType.覆盖原文件, CodeType.数据库启动代码);

	String baseServerPath = "/source-generator/src/main/java/com/heaven/jane/sourcegenerator/business";
	String baseFrontPath = "/source-generator-ui/src";

	/**
	 * 模板名
	 */
	@Getter
	String name;

	/**
	 * 相对路径或者文件名 与模块名相关的模板此处是目录，对应文件需要根据模块名变化 与模块名无关的模板此处是文件，指向对应的文件
	 */
	@Getter
	String targetDir;

	/**
	 * 文件后缀
	 */
	@Getter
	String suffix;

	/**
	 * 模板构建方式
	 */
	@Getter
	TemplateCreateType createType;

	/**
	 * 代码类型 前端代码 后端代码
	 */
	@Getter
	CodeType type;

	TemplateType(String name, String targetDir, String suffix, TemplateCreateType createType, CodeType type) {
		this.name = name;
		if (CodeType.前端代码.equals(type)) {
			this.targetDir = baseFrontPath + targetDir;
		} else if (CodeType.后端代码.equals(type)) {
			this.targetDir = baseServerPath + targetDir;
		} else {
			this.targetDir = targetDir;
		}
		this.suffix = suffix;
		this.createType = createType;
		this.type = type;
	}
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
