package com.zc.inventory.dao;


/**
 * 描述:
 * redis操作类(实际可以是jedis/redisTemplate等)
 *
 * @Author: zhangchao
 **/
public interface RedisDAO {

	void set(String key, String value);

	String get(String key);

	void delete(String key);
	
}
