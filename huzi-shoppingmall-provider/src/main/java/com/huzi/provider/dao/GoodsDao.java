package com.huzi.provider.dao;

import com.huzi.domain.Goods;
import org.apache.ibatis.annotations.Mapper;


public interface GoodsDao {

    Goods selectGoods(Integer goodsId);

    Integer insertGoods(Goods goods);

    Integer deleteGoodsById(Integer goodsId);
}
