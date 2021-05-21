package com.common.utils;

import java.util.*;

/**
 * Created by zzge163 on 2019/2/13.
 */
public class CollectionUtils {
    public CollectionUtils() {
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static <T extends Comparable> void sort(List<T> list) {
        Collections.sort(list);
    }

    public static Map<String, Object> mapKeysToUpperCase(Map<String, Object> map) {
        HashMap resultMap = new HashMap(16);
        Iterator var2 = map.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            resultMap.put(key.toUpperCase(), map.get(key));
        }

        return resultMap;
    }
}
