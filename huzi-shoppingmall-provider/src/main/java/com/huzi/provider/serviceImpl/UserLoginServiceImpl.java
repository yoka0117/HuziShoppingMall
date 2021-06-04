package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.domain.User;
import com.huzi.provider.dao.UserLoginDao;
import com.huzi.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Service(interfaceClass = UserLoginService.class,version = "1.0.0",timeout = 15000)
public class UserLoginServiceImpl implements UserLoginService {


    @Autowired
    private UserLoginDao userLoginDao;

    //通过账号和密码找到用户
    @Override
    public User selectUserByNameAndPassWord(User user) {
        return userLoginDao.checkUser(user);
    }


}
