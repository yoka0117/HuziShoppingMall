package com.huzi.provider.dao;

import com.huzi.domain.Inventory;
import com.huzi.domain.InventoryParam;

public interface InventoryDao {

    //添加商品库存
    int insertInventory(Inventory inventory);

    //根据skuId和warehouseId查询Inventory
    Inventory selectInventory(Inventory inventory);


    //增加商品库存
    int updateInventory(InventoryParam inventoryParam);

    //扣除真实库存(real)
    int updateInventoryCutReal(InventoryParam inventoryParam);


    //增加库存
    int updateInventoryAdd(InventoryParam inventoryParam);


    //扣除库存（物理）
    int updateInventoryCutPhysical(InventoryParam inventoryParam);


    //查inventory是否更新时间
    Inventory selectInventoryByUpdateTime();


}
