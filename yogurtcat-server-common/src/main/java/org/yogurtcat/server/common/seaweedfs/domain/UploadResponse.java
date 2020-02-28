package org.yogurtcat.server.common.seaweedfs.domain;

import lombok.Data;

/**
 * 上传响应
 * @author heaven
 *
 */
@Data
public class UploadResponse {
	
	/**
	 * 文件名
	 */
	private String name;
	
	/**
	 * 文件大小
	 */
	private Integer size;
	
	/**
	 * 文件标识
	 */
	private String fid;
	
	/**
	 * 文件url
	 */
	private String url;
}
