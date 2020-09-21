package com.zc.inventory.thread;

import com.zc.inventory.request.Request;
import com.zc.inventory.request.RequestQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述:
 * 请求处理线程池-单例
 * @Author: zhangchao
 **/
public class RequestProcessorThreadPool {
    //实际生成线程池参数/队列参数应当配置化而不是写死
    /**
     * 线程池
     */
    private ExecutorService threadPool =  Executors.newFixedThreadPool(10);

    public RequestProcessorThreadPool() {
        RequestQueue requestQueue = RequestQueue.getInstance();
        //初始化内存队列
        for (int j=1;j<=10 ;j++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            requestQueue.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }


    private static class Singleton {

        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance() {
            return instance;
        }

    }

    /**
     * 获取RequestProcessorThreadPool
     * @return
     */
    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化的便捷方法
     */
    public static void init() {
        getInstance();
    }

}
