package com.zc.inventory.dao.impl;

import com.zc.inventory.dao.RedisDAO;

/**
 * 描述:
 * todo 单机redis模式  用于本地测试...
 * @Author: zhangchao
 **/
public class RedisSingleDAOImpl implements RedisDAO {
    @Override
    public void set(String key, String value) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void delete(String key) {

    }
}
