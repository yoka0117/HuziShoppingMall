package com.huzi.common;

public enum PurchaseOrderStatus {
    INIT, //初始化
    FINISH,//完成进货订单
    INVALID,//进货订单作废
    LACK,//用户购买订单缺货
    SUCCESS,//用户购买订单预订成功
    DELIVERY //出库成功
}
