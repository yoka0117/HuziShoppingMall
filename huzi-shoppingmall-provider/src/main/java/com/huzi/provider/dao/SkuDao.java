package com.huzi.provider.dao;

import com.huzi.domain.Sku;
import com.huzi.domain.SkuGoods;

import java.util.List;

public interface SkuDao {

    //新增sku
    int insertSKU(Sku sku);


    //查询所有sku和goods
    List<SkuGoods> selectSkuAndGoods();
}
