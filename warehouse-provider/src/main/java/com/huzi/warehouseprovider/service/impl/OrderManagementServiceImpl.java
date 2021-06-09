package com.huzi.warehouseprovider.service.impl;

import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.UserOrder;
import com.huzi.service.OrderService;
import com.huzi.warehouseprovider.dao.OrderManagementDao;
import com.huzi.warehouseprovider.domain.OrderManagement;
import com.huzi.warehouseprovider.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class OrderManagementServiceImpl implements OrderManagementService {

    @Autowired
    private OrderManagementDao orderManagementDao;
    @Autowired
    OrderService orderService;

    //order订单预订成功后，推送至仓库，记录
    @Override
    public void insertOrderManagement(UserOrder userOrder) {
        OrderManagement orderManagement = new OrderManagement();
        orderManagement.setOrderId(userOrder.getOrderId());
        orderManagement.setOrderState(userOrder.getOrderState());
        orderManagement.setCreationTime(new Date());
        orderManagementDao.insertOrderManagement(orderManagement);
    }



    //每隔10秒就捞一次订单发货，并通知给商城
    @Override
    public void automaticDeliveryByXXLJOB() {
        //捞出订单为SUCCESS状态的订单
        List<OrderManagement> orderManagementList = orderManagementDao.selectOrderByState(PurchaseOrderStatus.SUCCESS.name());
        //此时模拟已经发货，并通知商城已发货
        for (OrderManagement orderManagement : orderManagementList) {
            //修改订单状态为DELIVERY（已出库）
            orderManagement.setOrderState(PurchaseOrderStatus.DELIVERY.name());
            orderManagementDao.updateOrderManagementState(orderManagement);

            UserOrder userOrder = new UserOrder();
            userOrder.setOrderId(orderManagement.getOrderId());
            //通知商城部分
            orderService.orderDelivery(userOrder);

        }
    }
}
