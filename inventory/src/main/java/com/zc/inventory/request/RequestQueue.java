package com.zc.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

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

    //<productId,前面是否有update请求>
    private Map<Integer,Boolean> flagMap = new ConcurrentHashMap();

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

    /**
     * 获取内存队列的数量
     * @return
     */
    public int queueSize() {
        return queues.size();
    }


    /**
     * 根据索引获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index) {
        return queues.get(index);
    }

    public Map<Integer,Boolean> getFlagMap() {
        return flagMap;
    }
}
