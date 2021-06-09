package com.huzi.warehouseprovider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.huzi.warehouseprovider.dao")
public class WarehouseProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseProviderApplication.class, args);
    }

}
