package org.yogurtcat.server.modules.monitor.operationlog.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.yogurtcat.server.common.constant.Module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 操作日志
 * 
 * @author heaven
 *
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 操作用户
	 */
	private String username;

    /**
     * 操作用户ip地址
     */
    private String ip;
    
    /**
     * 操作模块
     */
    private Module module;

	/**
	 * 操作内容
	 */
	private String content;

	/**
	 * 创建日期
	 */
	private Timestamp createTime;
    
    /**
     * 操作结果
     */
    private String result;
}
