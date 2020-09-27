package com.zc.localcache.service.impl;

import com.zc.localcache.model.ProductInfo;
import com.zc.localcache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @Author: zhangchao
 **/
@Service
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    @Override
    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地缓存中获取商品信息
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    public ProductInfo getLocalCache(Long id) {
        return null;
    }
}
