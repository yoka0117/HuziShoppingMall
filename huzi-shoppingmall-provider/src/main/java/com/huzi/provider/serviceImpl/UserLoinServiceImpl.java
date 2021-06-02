package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.domain.User;
import com.huzi.provider.dao.UserLoinDao;
import com.huzi.service.PurchaseOrderService;
import com.huzi.service.UserLoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Service(interfaceClass = UserLoinService.class,version = "1.0.0",timeout = 15000)
public class UserLoinServiceImpl implements UserLoinService {


    @Autowired
    private UserLoinDao userLoinDao;

    @Override
    public User selectUserByNameAndPassWord(User user) {
        return userLoinDao.checkUser(user);
    }
}
