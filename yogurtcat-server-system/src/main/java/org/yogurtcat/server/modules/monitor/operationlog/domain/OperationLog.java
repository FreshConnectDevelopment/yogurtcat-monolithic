package org.yogurtcat.server.modules.monitor.operationlog.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
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
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	@Enumerated(EnumType.STRING)
    private Module module;

	/**
	 * 国际化文件messages.properties对应的日志编码
	 */
	private String logCode;

	/**
	 * 国际化文件messages.properties对应的日志参数
	 */
    @Column(length = 2000)
	private Serializable arg;

    /**
     * 创建日期
     */
    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;
    
    /**
     * 操作结果
     */
    private String result;
}
