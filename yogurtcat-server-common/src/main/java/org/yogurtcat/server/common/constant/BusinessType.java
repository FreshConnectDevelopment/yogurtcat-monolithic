package org.yogurtcat.server.common.constant;

/**
 * 业务类型
 * 存储方式处绑定，目的是可以根据业务类型上传到相应的存储服务器
 * 目前是由系统定死，具体怎么划分还没想清楚
 * @author s
 *
 */
public enum BusinessType {
	/**
	 * 通用
	 */
	通用, 
	
	/**
	 * 音频格式有特殊需求情形，如转码
	 */
	音频, 
	
	/**
	 * 视频格式有特殊需求，如转码
	 */
	视频, 
	
	/**
	 * 资源需私有等情形
	 */
	机密, 
	
	/**
	 * 其它
	 */
	其它
}
