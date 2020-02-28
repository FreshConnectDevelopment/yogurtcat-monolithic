package org.yogurtcat.server.common.seaweedfs.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 本地存储删除数据包装类
 * @author s
 *
 */
@Data
@Builder
public class DeleteFilesOrDirectoryRequest {
	
	/**
	 * 文件服务器IP地址
	 */
	String serverIp;
	
	/**
	 * 文件或者目录路径
	 */
	private String path;
	
	/**
	 * 是否是目录
	 */
	private boolean isDirectory;

}
