package com.huzi.provider;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.huzi.provider.dao")   //扫描dao
@EnableDubboConfiguration  //开启dubbo配置
public class HuziShoppingmallProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuziShoppingmallProviderApplication.class, args);
    }

}
