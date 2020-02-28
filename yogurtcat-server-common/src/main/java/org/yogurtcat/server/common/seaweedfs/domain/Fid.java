package org.yogurtcat.server.common.seaweedfs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 文件信息Fid
 * @author s
 *
 */
@Data
public class Fid {

	@JsonProperty("volume_id")
	private int volumeId;

	@JsonProperty("file_key")
	private int fileKey;

	private long cookie;

}
