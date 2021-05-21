package com.common.utils;

import com.common.exception.ClientIpException;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzge163 on 2019/2/13.
 */
public class WebUtils {
    /**
     * description: getParamsToMap <br>
     * author: zhangzhe <br>
     * date: 2019/9/17 16:43 <br>
     *
     * @param request
     * @return java.util.Map<java.lang.String , java.lang.Object>
     */
    public static Map<String, Object> getParamsToMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap(16);
        Enumeration parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            map.put(name, request.getParameter(name));
        }

        return map;
    }

    /**
     * description: 获取客户端来源IP <br>
     * author: zhangzhe <br>
     * date: 2019/9/10 14:55 <br>
     *
     * @param
     * @return java.lang.String
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    throw new ClientIpException("获取客户端IP失败");
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
