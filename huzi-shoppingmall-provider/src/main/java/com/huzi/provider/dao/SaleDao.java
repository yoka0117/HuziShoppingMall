package com.huzi.provider.dao;


import com.huzi.domain.Sale;

import java.util.List;

public interface SaleDao {



    //添加销售表
    int addSale(Sale sale);


    //通过orderId查找sale对象
    List<Sale> selectSaleByOrderId(Sale sale);

    //通过orderId查找sale对象(number)
    List<Sale> selectSaleByOrderIdNumber(Integer orderId);


    //改变销售表状态（更改缺货、并填写缺多少货）
    int updateSaleState(Sale sale);


    //出库的时候，查看是否与订单相同
    Sale selectSaleBySkuIdAmount(Sale sale);

}
