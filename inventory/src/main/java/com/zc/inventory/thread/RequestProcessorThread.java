package com.zc.inventory.thread;

import com.zc.inventory.request.Request;

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

    @Override
    public Boolean call() throws Exception {
        while (true){
            //只要队列有请求则消费
            break;
        }
        return true;
    }
}
