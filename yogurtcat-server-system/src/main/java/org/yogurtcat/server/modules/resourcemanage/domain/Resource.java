package org.yogurtcat.server.modules.resourcemanage.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.yogurtcat.server.common.constant.BusinessType;
import org.yogurtcat.server.common.storage.domain.Storage;

import lombok.Getter;
import lombok.Setter;

/**
 * 资源实体
 * @author s
 *
 */
@Getter
@Setter
@Entity
public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	/**
	 * 上传文件名
	 */
	private String name;
	
	/**
	 * 存储名
	 */
	private String keyName;
	
	/**
	 * 文件类型
	 */
	private String type;

	/**
	 * 文件大小
	 */
	private Long size;
	
	/**
	 * 文件存储路径
	 */
	private String url;
	
	/**
	 * 存储方式
	 */
	@ManyToOne
	private Storage storage;
	
	/**
	 * 业务类型
	 */
	@Enumerated(EnumType.ORDINAL)
	private BusinessType businessType;
	
	/**
	 * 创建日期
	 */
	@CreationTimestamp
	private Date createDate;

}
