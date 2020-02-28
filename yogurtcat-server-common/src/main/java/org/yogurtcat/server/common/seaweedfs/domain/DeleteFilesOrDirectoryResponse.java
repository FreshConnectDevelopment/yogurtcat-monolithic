package org.yogurtcat.server.common.seaweedfs.domain;

import lombok.Data;

/**
 * 删除目录或者文件响应
 * @author heaven
 *
 */
@Data
public class DeleteFilesOrDirectoryResponse {
	
	/**
	 * 错误信息
	 */
	private String error;
}
