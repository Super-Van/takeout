package com.van.takeout.config;

import com.van.takeout.interceptor.*;
import com.van.takeout.util.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //员工的、用户的拦截器相互掣肘，分成两个应用会不会好点，设计两套接口，虽然逻辑高度重复，但从拦截来看通透许多
                registry.addInterceptor(new LoginInterceptor()).addPathPatterns(
                        "/employee/**",
                        "/common/**",
                        "/dish/**",
                        "/setmeal/**",
                        "/category/**",
                        "/order/page",//所有用户的订单
                        "/backend/page/**",
                        "/backend/index.html"
                ).excludePathPatterns(
                        "/employee/login",
                        "/backend/page/login/login.html"
                );
                registry.addInterceptor(new LogoutInterceptor()).addPathPatterns("/employee/logout");
                registry.addInterceptor(new RepeatLoginInterceptor()).addPathPatterns("/employee/login");
                //前后端分离，拦截静态资源请求没意义了，让前端Axios去拦
                registry.addInterceptor(new UserLoginInterceptor()).addPathPatterns(
                        "/user/**",
                        "/addressBook/**",
                        "/order/**",
                        "/shoppingCart/**"
                ).excludePathPatterns(
                        "/user/login",
                        "/user/sendMsg"
                );
                registry.addInterceptor(new UserRepeatLoginInterceptor()).addPathPatterns("/user/login");
                registry.addInterceptor(new UserLogoutInterceptor()).addPathPatterns("/user/logout");
            }

            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(0, new MappingJackson2HttpMessageConverter(new JacksonObjectMapper()));
            }
        };
    }
}
