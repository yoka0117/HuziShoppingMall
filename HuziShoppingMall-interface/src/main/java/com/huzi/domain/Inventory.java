package com.huzi.domain;

import java.io.Serializable;
import java.util.Date;

public class Inventory implements Serializable {

    private int inventoryId;
    private int skuId;
    private int warehouseId;
    private int physicalInventory;
    private int realInventory;
    private Date inventory_update_time;

    public Date getInventory_update_time() {
        return inventory_update_time;
    }

    public void setInventory_update_time(Date inventory_update_time) {
        this.inventory_update_time = inventory_update_time;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
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

    public int getPhysicalInventory() {
        return physicalInventory;
    }

    public void setPhysicalInventory(int physicalInventory) {
        this.physicalInventory = physicalInventory;
    }

    public int getRealInventory() {
        return realInventory;
    }

    public void setRealInventory(int realInventory) {
        this.realInventory = realInventory;
    }



}
