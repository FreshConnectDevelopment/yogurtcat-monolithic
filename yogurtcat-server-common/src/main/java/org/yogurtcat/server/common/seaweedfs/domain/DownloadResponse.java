package org.yogurtcat.server.common.seaweedfs.domain;

import java.io.File;

import lombok.Builder;
import lombok.Data;

/**
 * 文件下载响应
 * @author heaven
 *
 */
@Data
@Builder
public class DownloadResponse {
	
	/**
	 * 文件信息
	 */
	File file;

}
