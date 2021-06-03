package com.huzi.provider.dao;

import com.huzi.domain.Goods;
import com.huzi.domain.UserLoginInformation;


public interface UserLoginInformationDao {

    //查所有
    UserLoginInformation selectBySessionId(String sessionId);
}
