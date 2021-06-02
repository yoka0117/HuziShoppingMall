package com.huzi.consumer.interceptor;


import com.huzi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UserInterceptor implements HandlerInterceptor {

/*

    @Autowired
    private UserDao userDao ;
*/


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*//获得cookie
        Cookie[] cookie = request.getCookies();
        //判断是否有cookie，如果没有，重定向到登录页面
        if(cookie == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        //此时有cookie
        String login = null;
        for (Cookie cookies : cookie){
            //判断是否有叫"LOGIN"的key
            if ("userName".equals(cookies.getName())){
                //如果有，放行
                return true;
            }
        }
        response.sendRedirect(request.getContextPath()+"/login");*/

        User user = (User) request.getSession().getAttribute("user");
        if (null == user){
            //未登录
            response.sendRedirect(request.getContextPath() + "/noLogin");
            return false;
        }


        //已登录、放行
        return true;
    }
}
