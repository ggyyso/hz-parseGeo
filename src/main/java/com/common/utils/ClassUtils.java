package com.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zzge163 on 2019/2/14.
 */
public class ClassUtils {
    public ClassUtils() {
    }

    public static <T> T createInstance(Class<T> clz) throws IllegalAccessException, InstantiationException {
        T obj = clz.newInstance();
        return obj;
    }

    public static <T> Class<T> getClassGenricType(Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    public static Class getClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            return index < params.length && index >= 0 ? (!(params[index] instanceof Class) ? Object.class : (Class) params[index]) : Object.class;
        }
    }
}
