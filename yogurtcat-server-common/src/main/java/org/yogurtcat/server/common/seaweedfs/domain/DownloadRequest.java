package org.yogurtcat.server.common.seaweedfs.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 下载请求
 * @author heaven
 *
 */
@Data
@Builder
public class DownloadRequest {
	
	/**
	 * 文件名
	 */
	private String name;
	
	/**
	 * 保存文件路径
	 */
	private String storePath;
	
	/**
	 * 下载文件URL
	 */
	private String downloadUrl;

}
