package com.huzi.consumer.jobhandler;


import com.huzi.consumer.controller.sale.SaleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class demoHandler {

@Autowired
private SaleController saleController;

    @XxlJob("demoJobHandler")
    public void execute(String param) throws Exception {
        System.out.println("cool!!");
        saleController.reserve();
    }
}
