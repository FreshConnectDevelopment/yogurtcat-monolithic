package org.yogurtcat.server.common.payment.domain;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.yogurtcat.server.common.constant.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付配置实体VO类
 * @author s
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
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
	 * certStream
	 */
	private Byte[] certStream;
	
	/**
	 * 证书名
	 */
	private String certName;
	
	/**
	 * apiSecret
	 */
	private String apiSecret;

}
