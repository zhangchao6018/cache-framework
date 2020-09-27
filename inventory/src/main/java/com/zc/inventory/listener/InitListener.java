package com.zc.inventory.listener;

import com.zc.inventory.thread.RequestProcessorThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 描述:
 *
 * @Author: zhangchao
 **/
@WebListener//注册监听器
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //初始化工作线程和工作队列
        RequestProcessorThreadPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
