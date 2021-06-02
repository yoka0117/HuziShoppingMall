package com.huzi.consumer.controller.sale;


import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.*;
import com.huzi.service.InventoryService;
import com.huzi.service.OrderService;
import com.huzi.service.SaleService;
import com.huzi.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


//用户购买等相关功能
@Controller
@RequestMapping("/Sale")
public class SaleController {

    @Reference(interfaceClass = WarehouseService.class,version = "1.0.0" ,check = false)
    private WarehouseService warehouseService;

    @Reference(interfaceClass = OrderService.class,version = "1.0.0" ,check = false)
    private OrderService orderService;


    @Reference(interfaceClass = SaleService.class,version = "1.0.0" ,check = false)
    private SaleService saleService;

    @Reference(interfaceClass = InventoryService.class,version = "1.0.0" ,check = false)
    private InventoryService inventoryService;




    //添加销售表、订单表--------------------------------------------------------
    @RequestMapping("/addSale")
    public ModelAndView addSale(String skuId, String amount, Integer regionId, Integer userId, Integer shopId) {
        ModelAndView mv = new ModelAndView();
        String tip = "";
        //将拿到的数据，转换成String数组
        String[] skuIdList = skuId.split(",");
        String[] amountList = amount.split(",");
        //String[] shopIdList = shopId.split(",");
        //通过shopId和regionId找到合适的仓库Id
        WarehouseRegionMapper wrm = new WarehouseRegionMapper();
        wrm.setRegionId(regionId);
        wrm.setShopId(shopId);
        WarehouseRegionMapper warehouseRegionIds = warehouseService.selectMapper(wrm);
        //创建Order订单,并返回orderId
        Integer orderId = orderService.addOrder(warehouseRegionIds);
        if (orderId != 0) {
            //创建Sale对象
            //创建一个List对象装Sale对象
            List<Sale> saleList = new ArrayList<>();
            //给每个sale赋值
            for (int a = 0; a < skuIdList.length; a++) {
                Sale sale = new Sale();
                sale.setSkuId(Integer.parseInt(skuIdList[a]));
                sale.setAmount(Integer.parseInt(amountList[a]));
                sale.setShopId(shopId);
                sale.setOrderId(orderId);
                sale.setUserId(userId);
                sale.setSaleState(PurchaseOrderStatus.INIT.name());
                saleList.add(sale);
            }

            //将List<Sale>丢给service
            int result = saleService.addSale(saleList);
            if (result == 1) {
                tip = "添加销售单成功";
            } else {
                tip = "添加销售单失败";
            }

        }
        mv.addObject("result", tip);
        mv.setViewName("sale/insertSale");
        return mv;
    }
    //--------------------------------------------------------------------------



    //--------------------------------------------------------------------------
    //预定库存(支持部分预订) 老版

    @RequestMapping("/reserve")
    public ModelAndView reserve(){
        ModelAndView mv = new ModelAndView();
        String tip = null;
        int result = 0;

        result = orderService.reserve();

        if (result == 1){
            tip = "order不存在";
        }else if (result == 2){
            tip = "order已经为预定状态";
        }else if (result == 3){
            tip = "sale不存在";
        }else if (result == 4){
            tip = "库存不存在";
        }else if(result == 5 ){
            tip = "预定成功";
        }else if(result == 6 ){
            tip = "real库存还没有上新";
        }else if (result == 7){
            tip = "此订单还有预约没有完全完成";
        }
        mv.addObject("result" , tip);
        mv.setViewName("result");
        return mv;
    }


    //--------------------------------------------------------
    //取消预订
    @RequestMapping("/CancelReserve")
    public ModelAndView cancelReserve(Integer orderId){
        ModelAndView mv = new ModelAndView();
        String tip = "";

        UserOrder order = new UserOrder();
        order.setOrderId(orderId);
        int result = orderService.cancelReserve(order);
        if (result == 1){
            tip = "此order不存在";
        }else if(result == 2){
            tip = "此order状态为init";
        }else if (result == 3){
            tip = "取消预订成功";
        }

        mv.addObject("result",tip);
        mv.setViewName("result");
        return mv;
    }




    //----------------------------------------------------------
    //预订成功的订单出库
    @RequestMapping("/orderDelivery")
    public ModelAndView orderDelivery(Integer orderId, String skuId, String warehouseId, String amount){
        ModelAndView mv = new ModelAndView();
        String result = "";
        //将拿到的String转换成数组
        String[] skuIds = skuId.split(",");
        String[] warehouseIds = warehouseId.split(",");
        String[] amounts = amount.split(",");

        //List<订单发货对象>
        List<OrderDelivery> orderDeliveryList = new ArrayList<>();
        for (int i = 0 ; i < skuIds.length ; i ++) {
            OrderDelivery orderDelivery = new OrderDelivery();
            orderDelivery.setSkuId(Integer.parseInt(skuIds[i]));
            orderDelivery.setAmount(Integer.parseInt(amounts[i]));
            orderDelivery.setWarehouseId(Integer.parseInt(warehouseIds[i]));
            orderDeliveryList.add(orderDelivery);
        }

        //订单出库确认
        int tip = orderService.orderDelivery(orderId,orderDeliveryList);

        if (tip == 0){
            result = "成功出库";
        }else if (tip == 1){
            result="order订单不存在";
        }
        else if (tip == 2){
            result = "订单状态不属于预订成功";
        }
        else if (tip == 3){
            result = "出货单与详情单不同";
        }

        mv.addObject("result",result);
        mv.setViewName("result");
        return mv;
    }





    //------------------------------------------------------------------------------

    //预定库存(支持部分预订)新版
    //自动预订去除success状态的订单order
    //只查询init的订单 处理
    //如果进货了，就会将相关缺货订单补上
    //库存表添加 “补货时间” 字段
    //sale表添加 “仓库id”  字段
    @RequestMapping("/reserve2")
    public ModelAndView reserve2(){
        ModelAndView mv = new ModelAndView();
        String tip = null;
        int result = 0;
        result = orderService.reserve2();
        //查询一下最近1分钟库存有没有更新
        if (inventoryService.selectInventoryUpdateTime() != null) {
            //此时显示有更新
            orderService.reserve3();
        }

        mv.addObject("result" , tip);
        mv.setViewName("result");
        return mv;

    }


}
