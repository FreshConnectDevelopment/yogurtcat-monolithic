package org.yogurtcat.server.common.message.domain;

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
import org.yogurtcat.server.common.constant.MessageType;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息配置实体
 * @author s
 *
 */
@Getter
@Setter
@Entity
public class MessageConfig implements Serializable {

private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 存储服务类型
	 */
	@Enumerated(EnumType.ORDINAL)
	private MessageType type;
	
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
