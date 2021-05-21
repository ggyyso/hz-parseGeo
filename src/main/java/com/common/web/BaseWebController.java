package com.common.web;

import com.common.utils.JsonUtils;
import com.common.utils.WebUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by zzge163 on 2019/2/13.
 */
@Controller
@Scope("prototype")
public abstract class BaseWebController {
    public static final String SESSION_USER_INFO = "session_user_info";
    protected final String MSG = "msg";
    @Autowired
    protected ObjectMapper objectMapper;

    public BaseWebController() {
    }

    protected Map<String, Object> getParams(HttpServletRequest request) {
        return WebUtils.getParamsToMap(request);
    }

    protected String getPath(HttpServletRequest request, String path) {
        String rootPath = request.getServletContext().getRealPath("/");
        return rootPath + path;
    }

    protected String toJsonStr(Object obj) {
        return JsonUtils.toJson(this.objectMapper, obj, false);
    }

    protected <T> T toBen(String jsonStr) {
        return JsonUtils.json2GenericObject(this.objectMapper, jsonStr, new TypeReference<T>() {
        });
    }

    protected void clearSession(HttpSession session) {
        Enumeration names = session.getAttributeNames();

        while(names.hasMoreElements()) {
            String name = (String)names.nextElement();
            session.removeAttribute(name);
        }

    }

    protected String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                InetAddress inet = null;

                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException var5) {
                    var5.printStackTrace();
                }

                ipAddress = inet.getHostAddress();
            }
        }

        if(ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }
}