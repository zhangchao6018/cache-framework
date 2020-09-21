package com.zc.inventory.request;

/**
 * 描述:
 * 请求相关接口
 * @Author: zhangchao
 **/
public interface Request {
    void process();

    Integer getProductId();

    /**
     * 由于只有refresh缓存的操作需要调用方法,故写一个默认实现
     * @return
     */
    default boolean isForceRefresh(){
        return false;
    }
}
