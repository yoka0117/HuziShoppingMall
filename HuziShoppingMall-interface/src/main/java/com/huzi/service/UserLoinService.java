package com.huzi.service;


import com.huzi.domain.User;

public interface UserLoinService {

    //检查用户账号与密码是否正确
    User selectUserByNameAndPassWord(User user);
}
