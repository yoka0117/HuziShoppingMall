package com.huzi.provider.aspect;
import com.huzi.domain.Goods;
import com.huzi.service.UserLoginService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


//aspectJ框架中的注解，表示当前类为切面类
@Component
@Aspect
public class MyAspect {


    //定义方法，实现切面功能
    //定义要求：公共方法；没有返回值；方法名自定义，可以没参数，可以有参数（参数不能自定义）

    @Autowired
    private UserLoginService userLoginService;


    //execution(public * com.huzi.provider.serviceImpl.GoodsServiceImpl.selectGoodsById(Integer))
    @Pointcut("@annotation(com.huzi.annotation.MyAnnotation)")
    public void checkAdmin(){}



    //ProceedingJoinPoint 等同于Method，执行目标方法的
    //返回值：就是目标方法的执行结果
    @Around("checkAdmin()")
    public Object myBefore(ProceedingJoinPoint pjp) throws Throwable {

        Object result = null;

        //目标方法前
        System.out.println(new Date());
        //访问权限验证
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if ("sessionId".equals(cookie.getName())){
                cookie.getValue();
                //根据用户名查权限
                String 权限名 = "";

                if ("Order_service".equals(权限名)){
                    //执行目标方法
                    result = pjp.proceed();
                }
                else {
                   //报错

                }
            }
        }


        //目标方法后
        System.out.println(new Date());

        //返回目标方法返回结果
        return  result;
    }


}
