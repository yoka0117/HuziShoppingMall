package com.huzi.service;

import com.huzi.domain.UserOrder;
import com.huzi.domain.WarehouseRegionMapper;


import java.util.List;

public interface OrderService {


    //添加订单表
    int addOrder(WarehouseRegionMapper warehouseRegionMapper);


    //预定库存(支持部分预订)
    int reserve1();

    //取消预订
    int cancelReserve(UserOrder order);

    //订单出库确认
    //int orderDelivery(Integer orderId, List<OrderDelivery> orderDelivery);

    //订单出库确认
    void orderDelivery(UserOrder userOrder);

    /*****/
    void checkInventoryUpdateTime();
}
