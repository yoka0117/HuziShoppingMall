package com.huzi.consumer.controller.userLogin;


import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.domain.User;

import com.huzi.domain.UserLoginInformation;
import com.huzi.service.UserLoginInformationService;
import com.huzi.service.UserLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("user")
public class LoginController {

    @Reference(interfaceClass = UserLoginService.class,version = "1.0.0" ,check = false)
    private UserLoginService userLoginService;

    @Reference(interfaceClass = UserLoginInformationService.class,version = "1.0.0" ,check = false)
    private UserLoginInformationService userLoginInformationService;


    @RequestMapping("/login")
    public String  login(HttpServletResponse response, String userName, String passWord){

        //查看是否为null，账号、密码是否正确
        if (null == userName ||  null == passWord ) return "login/error";
        User user = new User();
        user.setUserName(userName);
        user.setPassWord(passWord);
        if (userLoginService.selectUserByNameAndPassWord(user) == null){
            return "login/error";
        }


        String sessionId = new Date() + userName;
        //得到几天后的时间
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.DAY_OF_YEAR,5);

        UserLoginInformation userLoginInformation = new UserLoginInformation();
        userLoginInformation.setSessionId(sessionId);
        userLoginInformation.setUserName(userName);
        userLoginInformation.setCreatTime(new Date());
        userLoginInformation.setEffectiveTime(now.getTime());
        userLoginInformationService.insertUserLoginInformation(userLoginInformation);

        Cookie cookie = new Cookie("sessionId",sessionId);
        response.addCookie(cookie);


        return "login/loginSuccess";


    }

}
