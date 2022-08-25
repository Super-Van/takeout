package com.van.takeout.interceptor;

import com.alibaba.fastjson.JSON;
import com.van.takeout.util.BaseContext;
import com.van.takeout.util.R;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long empId = (Long) request.getSession().getAttribute("employee");
        if (empId == null) {
            String uri = request.getRequestURI();
            //论理卖的东西谁都可以看，订单不是谁都能看，员工登录可看所有订单，用户登录只能看自己的订单
            if (uri.startsWith("/common") || uri.startsWith("/dish") || uri.startsWith("/category") || uri.startsWith("/setmeal") && request.getMethod().equals("GET")) {
                return true;
            }
            //response.sendRedirect("/backend/page/login/login.html");
            //依然交给客户端跳转到登录页，可惜比如直接从地址栏发get请求，就跳不到登录页了
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
        //ThreadLocal-线程的局部变量集，系统为诸线程（一个线程控制一次接收请求到发送响应）提供一块存储空间保存变量，各线程的这个空间相互隔离，合起来存在ThreadLocal对象中
        BaseContext.setId(empId);
        return true;
    }
}
