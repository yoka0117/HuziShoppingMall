package com.huzi.warehouseprovider.domain;

import java.util.Date;

public class OrderManagement {
    private Integer orderManagementId;
    private Integer orderId;
    private String orderState;
    private Date creationTime;


    public Integer getOrderManagementId() {
        return orderManagementId;
    }

    public void setOrderManagementId(Integer orderManagementId) {
        this.orderManagementId = orderManagementId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
