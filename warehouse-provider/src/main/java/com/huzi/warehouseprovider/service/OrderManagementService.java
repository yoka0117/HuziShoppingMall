package com.huzi.warehouseprovider.service;

import com.huzi.domain.UserOrder;

public interface OrderManagementService {

    //order订单预订成功后，推送至仓库，记录
    void insertOrderManagement(UserOrder userOrder);


    //每隔10秒就捞一次订单发货，并通知给商城
    void automaticDeliveryByXXLJOB();
}
