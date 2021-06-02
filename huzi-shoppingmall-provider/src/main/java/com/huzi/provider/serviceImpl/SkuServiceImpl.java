package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.domain.Sku;
import com.huzi.domain.SkuGoods;
import com.huzi.provider.dao.SkuDao;
import com.huzi.service.GoodsService;
import com.huzi.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Service(interfaceClass = SkuService.class,version = "1.0.0",timeout = 15000)
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuDao skuDao;

    //添加sku
    @Override
    public Integer insertSKU(Sku sku) {
        return skuDao.insertSKU(sku);
    }

    //查询所有sku和goods
    @Override
    public List<SkuGoods> selectGoodsAndSku() {
        return skuDao.selectSkuAndGoods();
    }


}
