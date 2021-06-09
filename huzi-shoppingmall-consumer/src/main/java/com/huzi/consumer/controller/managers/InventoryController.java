package com.huzi.consumer.controller.managers;


import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.domain.Inventory;
import com.huzi.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


//商品库存相关功能---2021.6.8
@Controller
@RequestMapping("inventory")
public class InventoryController {


    @Reference(interfaceClass = InventoryService.class,version = "1.0.0" ,check = false)
    private InventoryService inventoryService;


    //添加商品库存
    @RequestMapping("/insertInventory")
    public ModelAndView insertInventory(Inventory inventory){
        ModelAndView mv = new ModelAndView();
        String tip = "新增失败";
        int num = inventoryService.insertInventory(inventory);
        if (num > 0 ) {
            tip = "新增成功";
        }
        mv.addObject("result",tip);
        mv.setViewName("Inventory/insertInventory");
        return mv;
    }




    //仓库管理员管理仓库订单 -----
    @RequestMapping("/inventoryState")
    public ModelAndView inventoryState(Integer purchaseId, Integer orderDetailsId){
        ModelAndView mv = new ModelAndView();
        String tip = "";
        tip = inventoryService.PurchaseOrderByUser(purchaseId,orderDetailsId);


        mv.addObject("result",tip);
        mv.setViewName("Inventory/inventoryState");
        return mv;
    }
}
