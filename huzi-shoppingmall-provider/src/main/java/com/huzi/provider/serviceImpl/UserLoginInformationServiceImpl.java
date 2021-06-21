package com.huzi.provider.serviceImpl;

import com.huzi.domain.UserLoginInformation;
import com.huzi.provider.dao.UserLoginInformationDao;
import com.huzi.service.SkuService;
import com.huzi.service.UserLoginInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserLoginInformationServiceImpl implements UserLoginInformationService {

    @Autowired
    private UserLoginInformationDao userLoginInformationDao;

    @Override
    public UserLoginInformation selectBySessionId(String sessionId) {
        return userLoginInformationDao.selectBySessionId(sessionId);
    }

    //添加用户登录信息
    @Override
    public int insertUserLoginInformation(UserLoginInformation userLoginInformation) {

        return userLoginInformationDao.insertUserLoginInformation(userLoginInformation);
    }



}
