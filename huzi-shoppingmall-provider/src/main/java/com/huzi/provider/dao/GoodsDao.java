package com.huzi.provider.dao;

import com.huzi.domain.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;


public interface GoodsDao {

    Goods selectGoods(Integer goodsId);

    Integer insertGoods(Goods goods);

    Integer deleteGoodsById(Integer goodsId);
}
