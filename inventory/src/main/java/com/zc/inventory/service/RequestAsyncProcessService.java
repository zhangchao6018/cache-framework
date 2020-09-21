package com.zc.inventory.service;

import com.zc.inventory.request.Request;

/**
 * 描述:
 * 负责controller请求异步执行的service
 * @Author: zhangchao
 **/
public interface RequestAsyncProcessService {

    void process(Request request);

}
