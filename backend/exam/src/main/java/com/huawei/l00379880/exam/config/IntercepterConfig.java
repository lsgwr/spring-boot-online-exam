/***********************************************************
 * @Description : 拦截器配置
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-22 08:21
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.config;

import com.huawei.l00379880.exam.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IntercepterConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截user下的api
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user/**").addPathPatterns("/exam/**");
    }

}
