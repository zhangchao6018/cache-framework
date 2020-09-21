package com.zc.inventory.request;

import com.zc.inventory.model.ProductInventory;
import com.zc.inventory.service.ProductInventoryService;

/**
 * 描述:
 * 重新加载商品库存的缓存
 * @Author: zhangchao
 **/
public class ProductInventoryCacheRefreshRequest implements Request{
    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;

    public ProductInventoryCacheRefreshRequest(Integer productId,
                                               ProductInventoryService productInventoryService
                                               ) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        //数据库查询最新库存
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);

        //将最新库存刷新到缓存
        productInventoryService.equals(productInventory);
    }

    public Integer getProductId() {
        return productId;
    }

}
