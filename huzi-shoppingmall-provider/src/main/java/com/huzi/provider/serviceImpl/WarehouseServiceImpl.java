package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.domain.WarehouseRegionMapper;
import com.huzi.provider.dao.WarehouseRegionMapperDao;
import com.huzi.service.SkuService;
import com.huzi.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = WarehouseService.class,version = "1.0.0",timeout = 15000)
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRegionMapperDao warehouseRegionMapperDao;


    //通过地区id和商店id，确定发货仓库id
    @Override
    public WarehouseRegionMapper selectMapper(WarehouseRegionMapper warehouseRegionMapper) {
        return warehouseRegionMapperDao.selectWarehouseRegionId(warehouseRegionMapper);
    }



}
