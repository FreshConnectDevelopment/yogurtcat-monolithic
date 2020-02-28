package org.yogurtcat.server.commons.response;

import org.yogurtcat.server.commons.constant.ResultCode;

import lombok.Builder;
import lombok.Data;

/**
 * 相应结果信息类
 * @author heaven
 *
 */
@Data
@Builder
public class ResponseResult {
	
	/**
	 * 响应编码
	 */
	@Builder.Default
	private Long code = ResultCode.NORMAL.getCode();
	
	/**
	 * 响应编码
	 */
	@Builder.Default
	private String message = ResultCode.NORMAL.getMessage();
	
	/**
	 * 返回结果
	 */
	private Object data;
}
