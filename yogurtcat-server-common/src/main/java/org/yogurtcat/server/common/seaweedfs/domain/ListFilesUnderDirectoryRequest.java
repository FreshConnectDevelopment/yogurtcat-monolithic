package org.yogurtcat.server.common.seaweedfs.domain;

import java.util.Optional;

import lombok.Builder;
import lombok.Data;

/**
 * 目录一览请求
 * @author heaven
 *
 */
@Data
@Builder
public class ListFilesUnderDirectoryRequest {
	
	/**
	 * 文件服务器IP地址
	 */
	String serverIp;
	
	/**
	 * 路径
	 */
	private String path;
	
	/**
	 * 上次检索最后一个文件
	 */
	@Builder.Default
	private Optional<String> lastFileName = Optional.empty();
	
	/**
	 * 每次检索查询数量
	 */
	@Builder.Default
	private Optional<Integer> limit = Optional.empty();
	
}
