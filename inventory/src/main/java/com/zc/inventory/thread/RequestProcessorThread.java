package com.zc.inventory.thread;

import com.zc.inventory.request.ProductInventoryCacheRefreshRequest;
import com.zc.inventory.request.ProductInventoryDBUpdateRequest;
import com.zc.inventory.request.Request;
import com.zc.inventory.request.RequestQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 描述:
 * 执行请求的工作线程
 * @Author: zhangchao
 **/
public class RequestProcessorThread implements Callable<Boolean> {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    /**
     * 缓存更新/查询执行前置逻辑
     * @return
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {
        try {
            while (true){
                //队里为空会阻塞
                Request request = queue.take();
                RequestQueue instance = RequestQueue.getInstance();

                if (request instanceof ProductInventoryDBUpdateRequest){
                    //todo 更新前置操作
                }else if (request instanceof ProductInventoryCacheRefreshRequest){
                    //todo 查询前置操作
                }

                request.process();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

        return true;
    }
}
