package com.common.utils;

import org.aspectj.lang.JoinPoint;

/**
 * description: AopUtils <br>
 * date: 2019/9/23 16:57 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class AopUtils {
    public static String genReqParams(JoinPoint joinPoint) {
        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                if (joinPoint.getArgs()[i] != null) {
                    sb.append(joinPoint.getArgs()[i].toString());
                    sb.append(";");
                }
            }
            params = sb.substring(0, sb.length() > 0 ? sb.length() - 1 : 0);
        }

        return params;
    }
}
