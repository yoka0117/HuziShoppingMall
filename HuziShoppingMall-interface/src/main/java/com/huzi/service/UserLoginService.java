package com.huzi.service;


import com.huzi.domain.User;
import com.huzi.domain.UserLoginInformation;

public interface UserLoginService {

    //检查用户账号与密码是否正确
    User selectUserByNameAndPassWord(User user);

    //根据userName查userId
    User selectUserByUserName(String userName);

}
