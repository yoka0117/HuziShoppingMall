package com.huzi.provider.dao;


import com.huzi.domain.WarehouseRegionMapper;


public interface WarehouseRegionMapperDao {

    //通过地区id和商店id，确定发货仓库id
    WarehouseRegionMapper selectWarehouseRegionId(WarehouseRegionMapper warehouseRegionMapper);
}
