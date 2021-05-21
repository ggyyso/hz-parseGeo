package com.common.utils;

import org.springframework.util.DigestUtils;

/**
 * description: Md5Utils <br>
 * date: 2020/1/10 14:24 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class Md5Utils {
    /**
     * 盐，用于混交md5
     */
    private static final String salt = "sxgis";

    /**
     * description: 生成md5 <br>
     * author: zhangzhe <br>
     * date: 2020/1/10 14:25 <br>
     *
     * @param str
     * @return java.lang.String
     */
    public static String getMD5(String str) {
        String base = str + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());

        return md5;
    }

    public static void main(String[] args){
        String pwd = Md5Utils.getMD5("Passw0rd");
        System.out.println(pwd);
    }
}
