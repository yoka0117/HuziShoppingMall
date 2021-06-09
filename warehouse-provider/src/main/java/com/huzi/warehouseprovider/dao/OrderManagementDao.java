package com.huzi.warehouseprovider.dao;


import com.huzi.domain.UserOrder;
import com.huzi.warehouseprovider.domain.OrderManagement;

import java.util.List;

public interface OrderManagementDao {


    //order订单预订成功后，推送至仓库，记录
    int insertOrderManagement(OrderManagement orderManagement);


    //捞出订单为SUCCESS状态的订单
    List<OrderManagement> selectOrderByState(String orderState);



    //修改订单状态
    OrderManagement updateOrderManagementState(OrderManagement orderManagement);
}
