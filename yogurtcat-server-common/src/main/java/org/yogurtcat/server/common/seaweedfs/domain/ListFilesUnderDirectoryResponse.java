package org.yogurtcat.server.common.seaweedfs.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 目录一览
 * @author heaven
 *
 */
@Data
public class ListFilesUnderDirectoryResponse {
	
	/**
	 * 目录路径
	 */
	@JsonProperty("Path")
	private String path;
	
	/**
	 * 目录或者文件表项列表
	 */
	@JsonProperty("Entries")
	private List<Entry> entries;
	
	/**
	 * 显示数量
	 */
	@JsonProperty("Limit")
	private int limit;
	
	/**
	 * 最后文件数
	 */
	@JsonProperty("LastFileName")
	private String lastFileName;
	
	/**
	 * 是否展示更多
	 */
	@JsonProperty("ShouldDisplayLoadMore")
	private boolean shouldDisplayLoadMore;
	

}
