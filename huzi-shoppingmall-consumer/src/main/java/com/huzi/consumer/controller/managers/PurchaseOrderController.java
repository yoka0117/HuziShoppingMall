package com.huzi.consumer.controller.managers;


import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.domain.OrderDetails;
import com.huzi.domain.PurchaseOrder;
import com.huzi.service.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;


//仓库管理员管理商品的采购
@Controller
@RequestMapping("/purchase")
public class PurchaseOrderController {

    @Reference(interfaceClass = PurchaseOrderService.class,version = "1.0.0" ,check = false)
    private PurchaseOrderService purchaseOrderService;



    // 新增采购单
    @RequestMapping("/insertPurchase")
    public ModelAndView insertPurchase(String skuId , String warehouseId, String amount){
        ModelAndView mv  = new ModelAndView();
        String tip = "";
        //第一步：新建空白PurchaseOrder对象，（赋值创建时间和订单状态）在数据库中添加
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        //返回purchaseId
        int purchaseId = purchaseOrderService.insertPurchase(purchaseOrder);

        if(purchaseId != 0){
            //第二步：将前端传入的String，转换成int数组，给对象赋值，传入创建订单明细表
            List<OrderDetails> orderDetailsList = new ArrayList<>();
            String[] skuIdArray = skuId.split(",");
            String[] warehouseIdArray = warehouseId.split(",");
            String[] amountArray = amount.split(",");
            for(int i = 0 ; i < skuIdArray.length ; i++){
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setPurchaseId(purchaseId);
                orderDetails.setSkuId(Integer.parseInt(skuIdArray[i]));
                orderDetails.setWarehouseId(Integer.parseInt(warehouseIdArray[i]));
                orderDetails.setAmount(Integer.parseInt(amountArray[i]));
                orderDetailsList.add(orderDetails);
            }
            int result  =  purchaseOrderService.insertOrderDetails(orderDetailsList);

            if (result == 0){
                 tip = "创建明细表失败";
            } else {tip = "创建订单表/明细表成功";}
        }else {
            tip = "创建订单表失败";
        }
        mv.addObject("result",tip);
        mv.setViewName("purchaseOrder/insertPurchaseOrder");
        return mv;

    }



    //查询采购单列表及详情(根据采购单编号)
    @RequestMapping("/selectPurchase")
    public ModelAndView selectPurchase(Integer purchaseId){
        ModelAndView mv = new ModelAndView();
        PurchaseOrder purchaseOrder =  purchaseOrderService.selectPurchaseOrderAndDetails(purchaseId);
        mv.addObject("result",purchaseOrder);
        mv.setViewName("purchaseOrder/PurchaseOrderDetails");
        return  mv;
    }



    //作废订单(根据采购单编号)
    @RequestMapping("/invalidPurchase")
    public ModelAndView invalidPurchase(Integer purchaseId){
        ModelAndView mv = new ModelAndView();
        String result = purchaseOrderService.invalidPurchase(purchaseId);
        mv.addObject("result",result);
        mv.setViewName("purchaseOrder/invalidPurchase");
        return mv;
    }



    /**3完结采购单：
     * （1）查询是否存在采购单号
     * （2）检查订单是否完结
     * （3）更改订单状态、更新时间
     * （4）库存增加
     *              查skuid+仓库id：    有，更新。          没有，新增
    **/
    //完结采购单
    @RequestMapping("/completePurchase")
    public ModelAndView completePurchase(OrderDetails orderDetails){
        ModelAndView mv = new ModelAndView();
        String result = purchaseOrderService.completePurchase(orderDetails);
        mv.addObject("result",result);
        mv.setViewName("purchaseOrder/completePurchase");
        return  mv;
    }

}
