package com.common.utils;

import com.common.exception.DataConverterException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zzge163 on 2019/2/13.
 */
public class DateUtils {
    public DateUtils() {
    }

    public static Date parseDate(String formateStr, String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formateStr);
        return sdf.parse(dateStr);
    }

    public static Date parseYear(String dateStr) {
        try {
            return parseDate("yyyy", dateStr);
        } catch (ParseException e) {
            throw new DataConverterException("时间字段串转化错误", e);
        }
    }

    public static Date parseDate(String dateStr) {
        try {
            dateStr = dateStr.replace('/','-');
            return parseDate("yyyy-MM-dd", dateStr);
        } catch (ParseException e) {
            throw new DataConverterException("时间字段串转化错误", e);
        }
    }

    public static Date parseTime(String dateStr) {
        try {
            return parseDate("HH:mm:ss", dateStr);
        } catch (ParseException e) {
            throw new DataConverterException("时间字段串转化错误", e);
        }
    }

    public static Date parseDateTime(String dateStr) {
        try {
            return parseDate("yyyy-MM-dd HH:mm:ss", dateStr);
        } catch (ParseException e) {
            throw new DataConverterException("时间字段串转化错误", e);
        }
    }

    public static String formateDate(Date date, String formateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formateStr);
        return sdf.format(date);
    }

    public static String formateDate(Date date) {
        return formateDate(date, "yyyy-MM-dd");
    }

    public static String formateTime(Date date) {
        return formateDate(date, "HH:mm");
    }

    public static String formateDateTime(Date date) {
        return formateDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formateTimeFilePath(Date date) {
        return formateDate(date, "yyyyMMddHHmmss");
    }

    public static String formateMillTimeFilePath(Date date) {
        return formateDate(date, "yyyyMMddHHmmssSSS");
    }

    public static int subDateOfMinute(Date date1, Date date2) {
        int minute = subDateOfSecond(date1, date2) / 60;
        return minute;
    }

    public static int subDateOfSecond(Date date1, Date date2) {
        int second = Math.abs((int) (date1.getTime() - date2.getTime())) / 1000;
        return second;
    }

    public static List<String> getYearsBetween(Date minDate, Date maxDate) {
        List<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");//格式化为年
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.YEAR, 1);
        }
        return result;
    }

    /**
     * 时间计算
     *
     * @param days
     * @return
     */
    public static Date minusDateByDay(Integer days) {
        //使用默认时区和语言环境获得一个日历
        Calendar cal = Calendar.getInstance();
        //取当前日期的前n天
        cal.add(Calendar.DAY_OF_MONTH, days);
        // 格式转换
        Date date = cal.getTime();

        return date;
    }

    public static void main(String[] args) {
        DateUtils.parseDate("2020/11/13");
        DateUtils.parseDate("2020/11/13");
    }
}
