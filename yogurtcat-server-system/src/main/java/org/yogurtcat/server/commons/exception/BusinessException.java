package org.yogurtcat.server.commons.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 * @author heaven
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
public class BusinessException extends Throwable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 错误编码
	 */
	private Long code;
	
	/**
	 * 错误信息
	 */
	private String message;


}
