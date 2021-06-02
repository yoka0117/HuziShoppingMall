package com.huzi.service;

import com.huzi.domain.OrderDelivery;
import com.huzi.domain.UserOrder;
import com.huzi.domain.WarehouseRegionMapper;


import java.util.List;

public interface OrderService {


    //添加订单表
    int addOrder(WarehouseRegionMapper warehouseRegionMapper);


    //预定库存(支持部分预订)
    int reserve();

    //取消预订
    int cancelReserve(UserOrder order);

    //订单出库确认
    int orderDelivery(Integer orderId, List<OrderDelivery> orderDelivery);



    //预订库存（支持部分预订） 新版
    //2021.4.29版本
    int reserve2();
    int reserve3();
}
