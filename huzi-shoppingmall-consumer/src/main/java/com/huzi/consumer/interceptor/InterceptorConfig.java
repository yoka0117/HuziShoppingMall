package com.huzi.consumer.interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//定义此类是配置文件
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //拦截user下的所有访问请求，必须用户登录后才可访问
        String[] addPathPatterns ={
                "/huzi/**"
        };

        //排除哪些不用拦截
        String[] excludePathPatterns ={
                "/huzi/user/out","/huzi/user/error","/huzi/user/login"
        };

        UserInterceptor userInterceptor = new UserInterceptor();
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(userInterceptor);
        interceptorRegistration.addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);


    }
}
