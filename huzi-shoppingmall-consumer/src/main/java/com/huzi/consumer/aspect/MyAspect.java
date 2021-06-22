package com.huzi.consumer.aspect;
import com.huzi.common.MyException;
import com.huzi.consumer.aspect.annotation.MyAnnotation;
import com.huzi.domain.User;
import com.huzi.domain.UserLoginInformation;
import com.huzi.domain.permission.Permission;
import com.huzi.domain.permission.UserPermission;
import com.huzi.service.UserLoginInformationService;
import com.huzi.service.UserLoginService;
import com.huzi.service.permission.PermissionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


//aspectJ框架中的注解，表示当前类为切面类
@Component
@Aspect
public class MyAspect {


    //定义方法，实现切面功能
    //定义要求：公共方法；没有返回值；方法名自定义，可以没参数，可以有参数（参数不能自定义）

    @Autowired
    private UserLoginInformationService userLoginInformationService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private PermissionService permissionService;


    //execution(public * com.huzi.provider.serviceImpl.GoodsServiceImpl.selectGoodsById(Integer))
    @Pointcut("@annotation(com.huzi.consumer.aspect.annotation.MyAnnotation)")
    public void checkAdmin(){

    }



    //ProceedingJoinPoint 等同于Method，执行目标方法的
    //返回值：就是目标方法的执行结果
    @Around("checkAdmin()")
    public Object myBefore(ProceedingJoinPoint pjp) throws Throwable {


        Object result = null;


        //访问权限验证
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if ("sessionId".equals(cookie.getName())){
                //根据sessionId值查用户名
                String value = cookie.getValue();
                UserLoginInformation uf = userLoginInformationService.selectBySessionId(value);
                if (null != uf ){
                    //根据用户名查用户
                    String userName = uf.getUserName();
                    User user = userLoginService.selectUserByUserName(userName);
                    if (null != user){
                        //根据用户id查权限
                        Integer userId = user.getId();
                        UserPermission userPermission = permissionService.selectUserPermissionByUserId(userId);
                        if (null != userPermission){
                            //根据permissionId 查 permissionCode
                            Permission permission = permissionService.selectPermissionByPermissionId(userPermission.getPermissionId());
                            if (null != permission){
                                //获取目标方法上注解的值
                                Signature signature = pjp.getSignature();
                                MethodSignature methodSignature = (MethodSignature) signature;
                                Method method = methodSignature.getMethod();
                                Class clazz = method.getClass();
                                String module = "";
                                if (clazz.isAnnotationPresent(MyAnnotation.class)){
                                    MyAnnotation myAnnotation = (MyAnnotation) clazz.getAnnotation(MyAnnotation.class);
                                     module = myAnnotation.value();
                                }
                                //比较权限是否相同
                                if (module.equals(permission.getPermissionCode())){
                                    //执行目标方法
                                    result = pjp.proceed();
                                }else {
                                    //报错
                                    throw  new  MyException("001","没有权限访问");
                                }
                            }
                        }
                    }
                }
            }
        }


        //目标方法后
        System.out.println(new Date());

        //返回目标方法返回结果
        return  result;
    }


}
