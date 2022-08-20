package com.van.takeout.interceptor;

import com.alibaba.fastjson.JSON;
import com.van.takeout.util.BaseContext;
import com.van.takeout.util.R;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().startsWith("/order") && request.getSession().getAttribute("employee") != null) {
            return true;
        }
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId == null) {
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
        BaseContext.setId(userId);
        return true;
    }
}
