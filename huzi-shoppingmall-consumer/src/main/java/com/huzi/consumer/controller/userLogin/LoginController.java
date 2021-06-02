package com.huzi.consumer.controller.userLogin;


import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.domain.User;
import com.huzi.service.GoodsService;
import com.huzi.service.UserLoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class LoginController {

    @Reference(interfaceClass = UserLoinService.class,version = "1.0.0" ,check = false)
    private UserLoinService userLoinService;




    @RequestMapping("/login")
    public String  login(HttpServletRequest request,String userName,String passWord){

        //查看是否为null，账号、密码是否正确
        if (null == userName ||  null == passWord ) return "login/error";
        User user = new User();
        user.setUserName(userName);
        user.setPassWord(passWord);
        if (userLoinService.selectUserByNameAndPassWord(user) == null){
            return "login/error";
        }
        //将用户的信息存入session中
        request.getSession().setAttribute("user",user);


        return "login/loginSuccess";


    }

}
