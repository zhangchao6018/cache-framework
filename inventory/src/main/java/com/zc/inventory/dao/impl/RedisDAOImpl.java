package com.zc.inventory.dao.impl;

import com.zc.inventory.dao.RedisDAO;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @Author: zhangchao
 **/
public class RedisDAOImpl implements RedisDAO {
    @Resource
    private JedisCluster jedisCluster;

    @Override
    public void set(String key, String value) {
        jedisCluster.set(key, value);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public void delete(String key) {
        jedisCluster.del(key);
    }

}
