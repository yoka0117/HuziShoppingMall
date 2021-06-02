package com.huzi.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PurchaseOrder implements Serializable {


    private int purchaseId;
    private Date purchaseCreateTime;
    private String purchaseState;
    private Date purchaseUpdateTime;
    private List<OrderDetails> orderDetails;

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Date getPurchaseCreateTime() {
        return purchaseCreateTime;
    }

    public void setPurchaseCreateTime(Date purchaseCreateTime) {
        this.purchaseCreateTime = purchaseCreateTime;
    }

    public String getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(String purchaseState) {
        this.purchaseState = purchaseState;
    }

    public Date getPurchaseUpdateTime() {
        return purchaseUpdateTime;
    }

    public void setPurchaseUpdateTime(Date purchaseUpdateTime) {
        this.purchaseUpdateTime = purchaseUpdateTime;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "purchaseId=" + purchaseId +
                ", purchaseCreateTime=" + purchaseCreateTime +
                ", purchaseState='" + purchaseState + '\'' +
                ", purchaseUpdateTime=" + purchaseUpdateTime +
                ", orderDetails=" + orderDetails +
                '}';
    }
}

