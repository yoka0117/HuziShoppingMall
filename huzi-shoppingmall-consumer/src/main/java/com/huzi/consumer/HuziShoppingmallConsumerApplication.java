package com.huzi.consumer;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration  //开启dubbo配置
public class HuziShoppingmallConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuziShoppingmallConsumerApplication.class, args);
    }

}
