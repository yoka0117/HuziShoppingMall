package com.huzi.service;

import com.huzi.domain.Sku;
import com.huzi.domain.SkuGoods;

import java.util.List;

public interface SkuService {

    //添加sku
    Integer insertSKU(Sku sku);

    //查询所有sku+goods
    List<SkuGoods> selectGoodsAndSku();
}
