package com.huzi.consumer.interceptor;


import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.domain.UserLoginInformation;
import com.huzi.service.UserLoginInformationService;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class UserInterceptor implements HandlerInterceptor {

    @Reference(interfaceClass = UserLoginInformationService.class,version = "1.0.0" ,check = false)
    private UserLoginInformationService userLoginInformationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取cookie
        Cookie[] cookie = request.getCookies();
        //判断是否有cookie，如果没有，重定向到登录页面
        if(cookie == null){
            response.sendRedirect(request.getContextPath() + "/user/login");
            return false;
        }
        //此时有cookie
        String login = null;
        for (Cookie cookies : cookie){
            //判断是否有叫"sessionID"的key
            if ("sessionId".equals(cookies.getName())){
                //如果有，比较value还有有效期
               UserLoginInformation userLoginInformation =userLoginInformationService.selectBySessionId(cookies.getValue());
                if (null != userLoginInformation ) {
                    Long effectiveTime = userLoginInformation.getEffectiveTime().getTime();
                    Long nowTime = new Date().getTime();
                    if (nowTime - effectiveTime < 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
