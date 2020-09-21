package com.zc.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 描述:
 * 请求内存队列
 * @Author: zhangchao
 **/
public class RequestQueue {
    /**
     * 内存队列
     */
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    private static class Singleton{
        private static RequestQueue instance;

        static {
            instance = new RequestQueue();
        }

        public static RequestQueue getInstance(){
            return instance;
        }
    }

    /**
     * 单例获取
     * @return
     */
    public static RequestQueue getInstance(){
        return Singleton.getInstance();
    }

    /**
     * 添加一个内存队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue){
        this.queues.add(queue);
    }
}
