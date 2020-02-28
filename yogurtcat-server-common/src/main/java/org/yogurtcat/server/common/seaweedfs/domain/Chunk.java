package org.yogurtcat.server.common.seaweedfs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 文件信息
 * @author heaven
 *
 */
@Data
public class Chunk {
	
	@JsonProperty("file_id")
	private String fileId;
	
	private int size;
	
	private Long mtime;
	
	@JsonProperty("e_tag")
	private String eTag;
	
	private Fid fid;
	
	
}
