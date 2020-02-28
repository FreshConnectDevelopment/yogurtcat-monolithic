package org.yogurtcat.server.common.seaweedfs.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 文件或者目录表项
 * @author heaven
 *
 */
@Data
public class Entry {
	@JsonProperty("FullPath")
	private String fullPath;
	
	@JsonProperty("Mtime")
	private Date mtime;
	
	@JsonProperty("Crtime")
	private Date crtime;
	
	@JsonProperty("Mode")
	private long mode;
	
	@JsonProperty("Uid")
	private int uid;
	
	@JsonProperty("Gid")
	private int gid;
	
	@JsonProperty("Mime")
	private String mime;
	
	@JsonProperty("Replication")
	private String replication;
	
	@JsonProperty("Collection")
	private String collection;
	
	@JsonProperty("TtlSec")
	private int ttlSec;
	
	@JsonProperty("UserName")
	private String userName;
	
	@JsonProperty("GroupNames")
	private String groupNames;
	
	@JsonProperty("SymlinkTarget")
	private String symlinkTarget;
	
	@JsonProperty("chunks")
	private List<Chunk> chunks;
	
	
}
