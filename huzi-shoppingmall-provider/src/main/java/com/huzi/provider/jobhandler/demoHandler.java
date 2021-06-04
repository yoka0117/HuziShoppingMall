package com.huzi.provider.jobhandler;
import com.huzi.service.OrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class demoHandler {

@Autowired
private OrderService orderService;

    @XxlJob("demoJobHandler")
    public void execute(String param) throws Exception {
        System.out.println("cool!!");
        orderService.reserve();
    }



    @XxlJob("demoJobHandler1")
    public void execute1(String param) throws Exception {
        System.out.println("cool!!");
        orderService.checkInventoryUpdateTime();
    }
}
