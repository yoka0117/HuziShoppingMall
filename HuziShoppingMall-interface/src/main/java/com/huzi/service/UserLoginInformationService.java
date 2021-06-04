package com.huzi.service;

import com.huzi.domain.Goods;
import com.huzi.domain.UserLoginInformation;

public interface UserLoginInformationService {

    //查所有
    UserLoginInformation selectBySessionId(String sessionId);

    //添加用户登录信息
    int insertUserLoginInformation(UserLoginInformation userLoginInformation);
}
