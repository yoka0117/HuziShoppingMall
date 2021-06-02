package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.domain.Goods;
import com.huzi.provider.dao.GoodsDao;
import com.huzi.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = GoodsService.class,version = "1.0.0",timeout = 15000)
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    //通过id查询商品
    @Override
    public Goods selectGoodsById(Integer id) {

        return goodsDao.selectGoods(id);
    }

    //添加商品
    @Override
    public Integer insertGoods(Goods goods) {

        return goodsDao.insertGoods(goods);
    }


    //删除商品
    @Override
    public Integer deleteGoodsById(Integer goodsId) {
        return goodsDao.deleteGoodsById(goodsId);
    }
}
