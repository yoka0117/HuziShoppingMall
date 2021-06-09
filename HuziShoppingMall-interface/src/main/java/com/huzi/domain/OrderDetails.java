package com.huzi.domain;


import java.io.Serializable;



//订单明细
public class OrderDetails implements Serializable {

    private int orderDetailsId;
    private int purchaseId;
    private int skuId;
    private int warehouseId;
    private int amount;
    private String orderDetailsState;

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderDetailsState() {
        return orderDetailsState;
    }

    public void setOrderDetailsState(String orderDetailsState) {
        this.orderDetailsState = orderDetailsState;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderDetailsId=" + orderDetailsId +
                ", purchaseId=" + purchaseId +
                ", skuId=" + skuId +
                ", warehouseId=" + warehouseId +
                ", amount=" + amount +
                ", orderDetailsState='" + orderDetailsState + '\'' +
                '}';
    }
}
