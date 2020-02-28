package org.yogurtcat.server.common.seaweedfs.domain;

import java.io.File;

import lombok.Builder;
import lombok.Data;

/**
 * 上传请求
 * @author heaven
 *
 */
@Data
@Builder
public class UploadRequest {
	
	/**
	 * 文件服务器IP地址
	 */
	String serverIp;
	
	/**
	 * 待上传文件
	 */
	private File file;
	
	/**
	 * 文件上传路径
	 */
	private String path;
}
