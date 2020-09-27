package com.zc.inventory.request;

import com.zc.inventory.model.ProductInventory;
import com.zc.inventory.service.ProductInventoryService;

import java.util.concurrent.TimeUnit;

/**
 * 描述:
 * 商品发生交易-扣减库存
 *
 * cache aside pattern
 * 1.删除缓存
 * 2.更新数据库
 *
 * @Author: zhangchao
 **/
public class ProductInventoryDBUpdateRequest implements Request{
    /**
     * 商品库存
     */
    private ProductInventory productInventory;

    /**
     * 商品库存操作Service
     */
    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory,
                                           ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }


    @Override
    public void process() {

        try { TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}

        //1.删除redis中的缓存
        productInventoryService.removeProductInventoryCache(productInventory);

        //2.修改数据库中的库存
        productInventoryService.updateProductInventory(productInventory);
    }

    /**
     * 获取商品id
     */
    public Integer getProductId() {
        return productInventory.getProductId();
    }

}
