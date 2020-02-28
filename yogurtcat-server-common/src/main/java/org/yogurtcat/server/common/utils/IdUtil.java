package org.yogurtcat.server.common.utils;

import java.util.UUID;

import org.yogurtcat.server.common.utils.lang.ObjectId;
import org.yogurtcat.server.common.utils.lang.Snowflake;

/**
 * ID生成器工具类
 * @author s
 *
 */
public class IdUtil {

	/**
	 * 获取随机UUID
	 * 
	 * @return 随机UUID
	 */
	public static String randomUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 简化的UUID，去掉了横线
	 * 
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	
	/**
	 * 雪花算法生成id
	 * 最长19位，有时间顺序
	 * @return Long
	 */
	public static Long createSnowId() {
		return Snowflake.generateId();
	}
	
	/**
	 * 雪花算法生成id
	 * 最长19位，有时间顺序
	 * @return String
	 */
	public static String getSnowId() {
		return Snowflake.nextIdStr();
	}
	
	
	/**
	 * 创建MongoDB ID生成策略实现<br>
	 * ObjectId由以下几部分组成：
	 * 
	 * <pre>
	 * 1. Time 时间戳。
	 * 2. Machine 所在主机的唯一标识符，一般是机器主机名的散列值。
	 * 3. PID 进程ID。确保同一机器中不冲突
	 * 4. INC 自增计数器。确保同一秒内产生objectId的唯一性。
	 * </pre>
	 * 
	 * @return ObjectId
	 */
	public static String objectId() {
		return ObjectId.next();
	}
	
}





