package com.huzi.provider.dao;

import com.huzi.domain.User;
import com.huzi.domain.UserLoginInformation;

public interface UserLoginDao {

    //检查登录用户及密码
    User checkUser(User user);



    //根据userName 查 userId
    User selectUserByUserName(String userName);
}
