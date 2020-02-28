package org.yogurtcat.server.common.utils.lang;

import java.util.List;

import lombok.Data;

/**
 * 导出包装类
 * @author s
 *
 */
@Data
public class ExcelData {
	/**
	 * 导出文件名
	 */
	private String fileName;

	/**
	 * 标题
	 */
	private String[] head;
	
	/**
	 * 数据
	 */
	private List<String[]> data;
}
