package com.zc.inventory.controller;

import com.zc.inventory.model.ProductInventory;
import com.zc.inventory.request.ProductInventoryCacheRefreshRequest;
import com.zc.inventory.request.ProductInventoryDBUpdateRequest;
import com.zc.inventory.request.Request;
import com.zc.inventory.service.ProductInventoryService;
import com.zc.inventory.service.RequestAsyncProcessService;
import com.zc.inventory.vo.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 描述:
 * 商品库存Controller
 *
 * 模拟以下场景
 *（1）一个更新商品库存的请求过来，然后此时会先删除redis中的缓存，然后模拟卡顿5秒钟
 *（2）在这个卡顿的5秒钟内，我们发送一个商品缓存的读请求，因为此时redis中没有缓存，就会来请求将数据库中最新的数据刷新到缓存中
 *（3）此时读请求会路由到同一个内存队列中，阻塞住，不会执行
 *（4）等5秒钟过后，写请求完成了数据库的更新之后，读请求才会执行
 *（5）读请求执行的时候，会将最新的库存从数据库中查询出来，然后更新到缓存中
 * @Author: zhangchao
 **/
public class ProductInventoryController {

    @Resource
    private RequestAsyncProcessService requestAsyncProcessService;
    @Resource
    private ProductInventoryService productInventoryService;
    /**
     * 更新商品库存
     */
    @RequestMapping("/updateProductInventory")
    @ResponseBody
    public Response updateProductInventory(ProductInventory productInventory) {
        System.out.println("===========日志===========: 接收到更新商品库存的请求，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());

        Response response = null;

        try {
            //构造request
            Request request = new ProductInventoryDBUpdateRequest(productInventory,productInventoryService);
            //将request放入队列
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            response = new Response(Response.FAILURE);
        }

        return response;
    }

    /**
     * 获取商品库存
     */
    @RequestMapping("/getProductInventory")
    @ResponseBody
    public ProductInventory getProductInventory(Integer productId) {
        System.out.println("===========日志===========: 接收到一个商品库存的读请求，商品id=" + productId);
        ProductInventory productInventory = null;
        try {
            Request request = new ProductInventoryCacheRefreshRequest(productId,productInventoryService,false);
            requestAsyncProcessService.process(request);

            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;
            while(true) {
                //200ms还未刷到缓存的值
                if(waitTime > 200) {
                    break;
                }

                //尝试去redis中读取一次库存的缓存数据
                productInventory =  productInventoryService.getProductInventoryCache(productId);

                //如果读到了,直接返回
                if(productInventory != null){
                    System.out.println("===========日志===========: 在200ms内读取到了redis中的库存缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
                    return productInventory;
                }

                //如果没有读到,等待一段时间
                else {
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }
            }
            //200ms过去了,未获取到缓存值-->走mysql
            productInventory = productInventoryService.findProductInventory(productId);
            if(productInventory != null) {
                request = new ProductInventoryCacheRefreshRequest(productId,productInventoryService,true);
                requestAsyncProcessService.process(request);

                // 代码会运行到这里，只有三种情况：
                // 1、就是说，上一次也是读请求，数据刷入了redis，但是redis LRU算法给清理掉了，标志位还是false
                // 所以此时下一个读请求是从缓存中拿不到数据的，再放一个读Request进队列，让数据去刷新一下
                // 2、可能在200ms内，就是读请求在队列中一直积压着，没有等待到它执行（在实际生产环境中，基本是比较坑了）
                // 所以就直接查一次库，然后给队列里塞进去一个刷新缓存的请求
                // 3、数据库里本身就没有，缓存穿透，穿透redis，请求到达mysql库

                return productInventory;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //未命中,返回负值
        return new ProductInventory(productId, -1L);
    }
}
