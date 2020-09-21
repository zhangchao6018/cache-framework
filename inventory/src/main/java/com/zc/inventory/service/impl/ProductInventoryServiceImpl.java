package com.zc.inventory.service.impl;

import com.zc.inventory.dao.RedisDAO;
import com.zc.inventory.mapper.ProductInventoryMapper;
import com.zc.inventory.model.ProductInventory;
import com.zc.inventory.service.ProductInventoryService;

import javax.annotation.Resource;

/**
 * 描述:
 * 商品库存Service实现类
 *
 * @Author: zhangchao
 **/
public class ProductInventoryServiceImpl implements ProductInventoryService {
    @Resource
    private ProductInventoryMapper productInventoryMapper;
    @Resource
    private RedisDAO redisDAO;

    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.delete(key);
    }

    /**
     * 根据商品id查询商品库存
     * @param productId 商品id
     * @return 商品库存
     */
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    /**
     * 设置商品库存的缓存
     * @param productInventory 商品库存
     */
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.set(key, String.valueOf(productInventory.getInventoryCnt()));
    }
}
