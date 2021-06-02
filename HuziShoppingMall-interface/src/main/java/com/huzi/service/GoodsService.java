package com.huzi.service;

import com.huzi.domain.Goods;

public interface GoodsService {

    //根据goodsId查goods
    Goods selectGoodsById(Integer id);

    //添加goods商品
    Integer insertGoods(Goods goods);

    //删除goods商品
    Integer deleteGoodsById(Integer goodsId);
}
