package org.yogurtcat.server.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * redis操作类
 * @author heaven
 *
 */
@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate template;
	
	/**
	 * 获取内容
	 * @param key
	 * @return
	 */
	public String query(String key) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		return ops.get(key);
	}
	
	public void set(String key, String value) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		ops.set(key, value);
	}

}
