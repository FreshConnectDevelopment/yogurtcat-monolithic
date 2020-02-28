package org.yogurtcat.server.common.payment.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.yogurtcat.server.common.constant.PaymentType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 支付配置实体
 * @author s
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 支付类型
	 */
	@Enumerated(EnumType.ORDINAL)
	private PaymentType type;
	
	/**
	 * appID
	 */
	private String appId;
	
	/**
	 * mchID
	 */
	private String mchId;
	
	/**
	 * apiSecret
	 */
	private String apiSecret;

	/**
	 * certStream
	 */
	@JsonIgnore
	@Column(columnDefinition="blob")
	private byte[] certStream;
	
	/**
	 * 证书名
	 */
	private String certName;
	
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
