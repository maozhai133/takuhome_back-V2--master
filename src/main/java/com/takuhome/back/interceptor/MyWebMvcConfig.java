package com.takuhome.back.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @Title:MyWebMvcConfig
 * @Author:NekoTaku
 * @Date:2021/11/17 15:02
 * @Version:1.0
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Bean
    public MyInterceptor myInterceptor(){
        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截处理操作的匹配路径
        //放开静态拦截
        registry.addInterceptor(myInterceptor())
                .addPathPatterns("/**") //拦截所有路径
                .excludePathPatterns("/login", "/", "/exit", "/get_cpacha","/Register/**","/resetPassword/**")//排除路径
                .excludePathPatterns("/xadmin/**")
                .excludePathPatterns("/umedit/**")
                .excludePathPatterns("/imgsrc/**")
                .excludePathPatterns("/upload/**");//排除静态资源拦截

    }
}
