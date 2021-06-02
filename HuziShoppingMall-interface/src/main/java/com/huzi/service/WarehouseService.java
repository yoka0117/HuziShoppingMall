package com.huzi.service;

import com.huzi.domain.WarehouseRegionMapper;

public interface WarehouseService {


    //通过地区id和商店id，确定发货仓库id
    WarehouseRegionMapper selectMapper( WarehouseRegionMapper warehouseRegionMapper);



}
