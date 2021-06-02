package com.huzi.service;

import com.huzi.domain.Inventory;

public interface InventoryService {

    //添加商品库存
    Integer insertInventory(Inventory inventory);

    //仓库管理员管理仓库订单
    String PurchaseOrderByUser(Integer purchaseId, Integer orderDetailsId);

    //查Inventory（是否更新）
    Inventory selectInventoryUpdateTime();
}
