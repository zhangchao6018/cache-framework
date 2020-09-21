package com.zc.inventory.service.impl;

import com.zc.inventory.request.Request;
import com.zc.inventory.request.RequestQueue;
import com.zc.inventory.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 描述:
 * 负责controller请求异步执行的service实现类
 * @Author: zhangchao
 **/
@Service("requestAsyncProcessService")
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    /**
     * 将请求路由到指定内存队列
     * @param request
     */
    @Override
    public void process(Request request) {
        try {
            //根据每个请求的商品id],路由到对应内存队列
            ArrayBlockingQueue<Request> routingQueue = this.getRoutingQueue(request.getProductId());
            //将请求放入相应队列,完成路由的操作
            routingQueue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据商品ID获取路由到的内存队列
     * @param productId 商品id
     * @return 内存队列
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
        RequestQueue requestQueue = RequestQueue.getInstance();

        //现获取productId的hash值
        String key = String.valueOf(productId);
        int h ;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (requestQueue.queueSize()-1) & hash;

        System.out.println("===========日志===========: 路由内存队列，商品id=" + productId + ", 队列索引=" + index);
        //根据路由到的index获取工作队列
        return requestQueue.getQueue(index);
    }
}
