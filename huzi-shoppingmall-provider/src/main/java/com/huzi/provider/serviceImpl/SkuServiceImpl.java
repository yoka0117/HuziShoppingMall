package com.huzi.provider.serviceImpl;

import com.huzi.domain.Sku;
import com.huzi.domain.SkuGoods;
import com.huzi.provider.dao.SkuDao;
import com.huzi.service.GoodsService;
import com.huzi.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
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
