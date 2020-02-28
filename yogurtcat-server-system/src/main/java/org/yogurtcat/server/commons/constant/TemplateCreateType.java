package org.yogurtcat.server.commons.constant;

/**
 * 
 * 根据模板构建源码方式
 * 
 * 覆盖原文件：针对全局性的文件使用模板生成后直接覆盖
 * 仅生成文件：目录固定，只需要根据模板生成对应的源文件
 * 创建目录及文件：目录和源文件都需要生成
 * @author s
 *
 */
public enum TemplateCreateType {
	/**
	 * 覆盖原文件
	 */
	覆盖原文件,
	/**
	 * 仅生成文件
	 */
	仅生成文件,
	/**
	 * 创建目录及文件
	 */
	创建目录及文件;
}
