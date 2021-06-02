package com.huzi.provider.dao;

import com.huzi.domain.OrderDetails;
import com.huzi.domain.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderDao {

    //创建采购单---------------------------------------------
    // 创建采购单（赋值创建时间、订单状态）
    Integer insertPurchase(PurchaseOrder purchaseOrder);
    //创建采购单详情
    Integer insertDetails(OrderDetails orderDetails);
    //--------------------------------------------------------


    //根据采购单号查询详情------------------------------------
    //根据采购单号查询状态、创建时间、更新时间
    PurchaseOrder selectPurchaseOrderById(Integer purchaseId);
    //根据采购单号查询采购详情表（skuId、warehouseId、数量）
    List<OrderDetails> selectOrderDetailsByPurchaseId(Integer purchaseId);
    //---------------------------------------------------------



    //更改订单状态：作废订单
    Integer updatePurchase(PurchaseOrder purchaseOrder);


    //查询采购详情是否存在(通过详情id)
    OrderDetails selectOrderDetails(Integer orderDetailsId);


    //更改采购单详情表：状态
    int updateDetails(OrderDetails orderDetails);

}
