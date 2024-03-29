package com.huzi.consumer.interceptor;


import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.domain.UserLoginInformation;
import com.huzi.service.UserLoginInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class UserInterceptor implements HandlerInterceptor {

   @Autowired
    private UserLoginInformationService userLoginInformationService;
   @Autowired
   private RedisTemplate<Object,Object> redisTemplate;

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
                //如果有，比较value还有有效期(操作redis)
                Boolean isEmpty = redisTemplate.boundHashOps("loginKey").hasKey(cookies.getValue());
                if (isEmpty == true){
                    String value = (String) redisTemplate.boundHashOps("loginKey").get(cookies.getValue());
                    Long effectiveTime = Long.parseLong(value);
                    Long nowTime = new Date().getTime();
                    if (nowTime - effectiveTime < 0){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
