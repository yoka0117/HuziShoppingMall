package com.huzi.service;

import com.huzi.domain.OrderDetails;
import com.huzi.domain.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderService {

    //新增采购单
    int insertPurchase(PurchaseOrder purchaseOrder);
    int insertOrderDetails(List<OrderDetails> orderDetailsList);


    //根据采购单号，查询采购信息
    PurchaseOrder selectPurchaseOrderAndDetails(Integer purchaseId);


    //作废订单(根据采购单编号)
    String invalidPurchase(Integer purchaseId);




    //完结采购单
    String completePurchase(OrderDetails orderDetails);
}
