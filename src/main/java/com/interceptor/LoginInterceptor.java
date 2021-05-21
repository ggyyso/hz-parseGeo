package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model.TUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * description: LoginInterceptor 登录拦截器<br>
 * date: 2019/9/12 16:48 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    public static final String USER_KEY = "userid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler)
            throws Exception {
        // 识别ajax的响应头
        String requestType = request.getHeader("X-Requested-With");
        TUser user = (TUser) request.getSession().getAttribute("user");
        if (user != null) {
            logger.debug(user.getName() + "用户已登录");
            return super.preHandle(request, response, handler);
        } else {
            logger.debug("用户登录状态失效，需要重新登录。");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            // 如果是ajax类型，响应logout给前台
            if (requestType != null && "XMLHttpRequest".equals(requestType)) {
                response.getWriter().print("{\"code\":-1,\"info\":\"User not logged in or login expired\"}");
            } else {
                response.getWriter().print("{\"code\":-1,\"info\":\"User not logged in or login expired\"}");
            }
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

}
