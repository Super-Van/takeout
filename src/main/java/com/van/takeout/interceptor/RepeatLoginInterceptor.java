package com.van.takeout.interceptor;

import com.alibaba.fastjson.JSON;
import com.van.takeout.util.R;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RepeatLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("employee") == null) {
            return true;
        }
        response.getWriter().write(JSON.toJSONString(R.error("不可重复登录")));
        return false;
    }
}
