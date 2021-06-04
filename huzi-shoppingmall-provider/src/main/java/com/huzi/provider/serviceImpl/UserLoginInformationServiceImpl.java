package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.domain.UserLoginInformation;
import com.huzi.provider.dao.UserLoginInformationDao;
import com.huzi.service.SkuService;
import com.huzi.service.UserLoginInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = UserLoginInformationService.class,version = "1.0.0",timeout = 15000)
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
