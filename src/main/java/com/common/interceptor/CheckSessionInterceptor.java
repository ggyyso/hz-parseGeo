package com.common.interceptor;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.annotations.NoCheckSession;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * description: 登录拦截器 <br>
 * date: 2019/2/13 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class CheckSessionInterceptor implements HandlerInterceptor {
    public CheckSessionInterceptor() {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception exception) {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getContextPath();
        if (!request.getRequestURI().equals(path) && !request.getRequestURI().equals(path + "/")) {
            // HandlerMethod 封装方法定义相关的信息,如类,方法,参数等.
            // 如果没有mapping到具体方法，则此无法将handler对象转换为此类
            HandlerMethod handlerMethod = null;
            try {
                handlerMethod = (HandlerMethod) handler;
            } catch (ClassCastException var11) {
                return true;
            }

            NoCheckSession noCheckSession = (NoCheckSession) handlerMethod.getMethodAnnotation(NoCheckSession.class);
            if (noCheckSession == null && request.getSession().getAttribute("session_user_info") == null) {
                response.reset();
                response.setContentType("application/json; charset=UTF-8");
                PrintWriter pw = response.getWriter();
                pw.write("{\"code\" : -1, \"info\":\"SESSION超时或丢失\"}");
                pw.flush();
                pw.close();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}