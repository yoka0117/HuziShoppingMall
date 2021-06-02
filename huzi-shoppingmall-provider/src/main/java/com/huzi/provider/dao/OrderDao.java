package com.huzi.provider.dao;

import com.huzi.domain.Region;
import com.huzi.domain.UserOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface OrderDao {

    //添加订单
    int addOrder(UserOrder userOrder);


    //查所有
    List<UserOrder> selectOrderAll();


    //查order对象
    UserOrder selectOrder(UserOrder order);


    //更改order属性
    int updateOrder(UserOrder order);



    //根据状态查所有Order
    List<UserOrder> selectOrderByState(UserOrder order);


    //选仓
    Region selectRegionById(Integer regionId);
    //根据地区Id 选择可用仓库，把仓库Id填充到订单表
    //int addOrder(Order order);











}
