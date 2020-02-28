package org.yogurtcat.server.modules.rabbitmq.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RabbitMQ配置实体类
 * @author s
 *
 */
@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMqConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 主机
	 */
	@Column(name = "[host]")
	private String host;
	
	/**
	 * 端口
	 */
	@Column(name = "[port]")
	private int port;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 是否重试
	 */
	private boolean enabled;
	
	/**
	 * 重试初始间隔
	 */
	@Column(name = "[interval]")
	private Integer interval;
	
	/**
	 * 创建日期
	 */
	@UpdateTimestamp
	private Date lastModifyDate;
}
