package com.common.utils;

import com.common.exception.ParamException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description: IdUserUtils <br>
 * date: 2020/3/12 15:19 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class IdUserUtils {
    public static int IdNOToAge(String IdNO) {
        int leh = IdNO.length();
        String dates = "";
        try {
            if (leh == 18) {
                dates = IdNO.substring(6, 10);
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                String year = df.format(new Date());
                int u = Integer.parseInt(year) - Integer.parseInt(dates);
                return u;
            } else {
                dates = IdNO.substring(6, 8);
                return Integer.parseInt(dates);
            }
        } catch (Exception ex) {
            throw new ParamException("身份证号格式错误");
        }

    }
}