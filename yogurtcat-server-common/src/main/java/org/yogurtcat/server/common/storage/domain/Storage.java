package org.yogurtcat.server.common.storage.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.yogurtcat.server.common.constant.StorageType;

import lombok.Data;

/**
 * 存储配置实体
 * @author s
 *
 */
@Data
@Entity
public class Storage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 存储方式名
	 */
	private String name;
	
	/**
	 * 存储服务类型
	 */
	@Enumerated(EnumType.ORDINAL)
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
	@CreationTimestamp
	private Date createDate;

	/**
	 * 最后修改日期
	 */
	@UpdateTimestamp
	private Date updateDate;
}
