package com.huzi.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:dubbo-consumer.xml")  //开启dubbo配置
public class HuziShoppingmallConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuziShoppingmallConsumerApplication.class, args);
    }

}
