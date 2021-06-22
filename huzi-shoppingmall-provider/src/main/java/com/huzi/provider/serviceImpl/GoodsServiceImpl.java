package com.huzi.provider.serviceImpl;


import com.huzi.domain.Goods;
import com.huzi.provider.dao.GoodsDao;
import com.huzi.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
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
