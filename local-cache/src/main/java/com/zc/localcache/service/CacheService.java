package com.zc.localcache.service;

import com.zc.localcache.model.ProductInfo;

/**
 * 描述:
 * 本地缓存操作接口
 * @Author: zhangchao
 **/
public interface CacheService {
    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    ProductInfo saveLocalCache(ProductInfo productInfo);

    /**
     * 从本地缓存中获取商品信息
     * @param id
     * @return
     */
    ProductInfo getLocalCache(Long id);

}
