package com.czj.interceptor;

import com.czj.controller.util.CLog;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * 拦截没有PublicController注解的接口
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // START 方法注解级拦截器
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        PublicController publicController = method.getAnnotation(PublicController.class);
        // 非公开接口
        if (publicController == null) {
            HttpSession session = request .getSession();
            Object id = session.getAttribute("userId");
            if(null == id){
                CLog.log("登陆检测中..用户未登陆！");
                response.setHeader("content-type","text/html");
                // 重定向到登录界面
                response.sendRedirect("http://localhost:80/#/");
                return false;
            }
            CLog.log("登陆检测中..用户已登陆，id:"+(String) id);
            return true;
        }
        return true;
    }
}
