package com.huzi.provider.serviceImpl;

import com.huzi.domain.User;
import com.huzi.provider.dao.UserLoginDao;
import com.huzi.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class UserLoginServiceImpl implements UserLoginService {


    @Autowired
    private UserLoginDao userLoginDao;

    //通过账号和密码找到用户
    @Override
    public User selectUserByNameAndPassWord(User user) {
        return userLoginDao.checkUser(user);
    }


    //根据userName查userId
    @Override
    public User selectUserByUserName(String userName) {
        return userLoginDao.selectUserByUserName(userName);
    }


}
