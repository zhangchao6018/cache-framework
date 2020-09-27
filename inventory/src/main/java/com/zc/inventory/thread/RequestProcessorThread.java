package com.zc.inventory.thread;

import com.zc.inventory.request.ProductInventoryCacheRefreshRequest;
import com.zc.inventory.request.ProductInventoryDBUpdateRequest;
import com.zc.inventory.request.Request;
import com.zc.inventory.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 描述:
 * 【执行请求的工作线程】
 *
 * flag == null 情况分析
 * 假设我们要对库存进行预热加载到缓存,然后某productId一直都没有被访问,没有读没有写,因此其对应flagMap是无值的
 * 但是由于redis内存LRU机制等其他原因,key丢失了,导致后续等该商品真正的读请求过来的时候发现flag为null
 *
 *
 *
 * 假如说，执行完了一个读请求之后，假设数据已经刷新到redis中了
 * 但是后面可能redis中的数据会因为内存满了，被自动清理掉
 * 如果说数据从redis中被自动清理掉了以后
 * 然后后面又来一个读请求，此时如果进来，发现标志位是false，就不会去执行这个刷新的操作了
 * 所以在执行完这个读请求之后，实际上这个
 * 停留在false就不会走刷新缓存的方法,而是直接返回true;并且后续的读请求也会一直这样,controller需要处理该情况
 * 因此有了forceRefresh该标记,如果是true说明是强刷逻辑,直接入队然后刷到缓存即可,不用也不能走去重逻辑
 *
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
     * 当为强制刷缓存--直接放入队列执行查库-刷缓存
     * 否则:
     *      执行缓存更新/查询执行的前置操作--相同商品读请求去重
     * @return
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {
        try {
            while (true){
                //队里为空会阻塞
                Request request = queue.take();
                boolean forceRefresh = request.isForceRefresh();
                if (!forceRefresh){
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

                    if (request instanceof ProductInventoryDBUpdateRequest){
                        //1.更新操作 -- 将该商品设置为[正在修改]
                        flagMap.put(request.getProductId(),true);
                    }else if (request instanceof ProductInventoryCacheRefreshRequest){
                        //2.查询操作(去重查询)
                        Boolean flag = flagMap.get(request.getProductId());
                        if (flag == null){
                            //(1)有可能确实是不存在的productId
                            //(2)也可能在mysql真实存在,只是种种原因没设置到flagMap中去--分析见类注释
                            flagMap.put(request.getProductId(),false);
                        }

                        if (flag !=null && flag){
                            //值为true  表示现在队里中没有读请求-->将值置为false,让告知后续读请求
                            flagMap.put(request.getProductId(), false);
                        }

                        //如果flag为false,说明前面已经存在相同读请求,
                        if (flag != null && !flag){
                            //本次读请求不必再走下面的流程(即等待写完成,刷新mysql值到缓存)
                            //直接返回true在controller尝试获取缓存就行
                            return true;
                        }

                    }
                }
                if (request instanceof ProductInventoryDBUpdateRequest){
                    System.out.println("===========日志===========: 写请求，商品id=" + request.getProductId()+",request"+request);
                }else if (request instanceof ProductInventoryCacheRefreshRequest){
                    System.out.println("===========日志===========: 读请求，商品id=" + request.getProductId()+",request"+request);
                }

                //执行该request
                request.process();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
