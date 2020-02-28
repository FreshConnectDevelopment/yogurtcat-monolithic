package org.yogurtcat.server.modules.storage.domain;

import java.io.Serializable;
import java.util.Date;

import org.yogurtcat.server.common.constant.StorageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 存储实体VO
 * @author s
 *
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorageVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 存储方式名
	 */
	private String name;
	
	/**
	 * 存储服务类型
	 */
	private StorageType type;
	
	/**
	 * 关联业务类型
	 * 多个逗号拼接
	 */
	private String businessType;

	/**
	 * bucketname
	 */
	private String bucketname;
	
	/**
	 * 服务器IP
	 */
	private String ip;
	
	/**
	 * endpoint
	 * 本地存储路径
	 */
	private String endpoint;
	
	/**
	 * accessKeyId
	 */
	private String accessKeyId;
	
	/**
	 * accessKeySecret
	 */
	private String accessKeySecret;
	
	/**
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 最后修改日期
	 */
	private Date updateDate;
}
