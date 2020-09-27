package com.zc.localcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan//扫描监听器
@MapperScan("com.zc.localcache.mapper")
public class LocalCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalCacheApplication.class, args);
    }

}
