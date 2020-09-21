package com.zc.cacheframework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zc")
public class CacheFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheFrameworkApplication.class, args);
    }

}
