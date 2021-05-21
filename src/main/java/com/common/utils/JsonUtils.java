package com.common.utils;

/**
 * Created by zzge163 on 2019/2/13.
 */
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    public JsonUtils() {
    }

    public static <T> T json2GenericObject(ObjectMapper objectMapper, String jsonString, TypeReference<T> tr) {
        if(jsonString != null && !"".equals(jsonString)) {
            try {
                return objectMapper.readValue(jsonString, tr);
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String toJson(ObjectMapper objectMapper, Object object, boolean isPretty) {
        String jsonString = "";

        try {
            if(isPretty) {
                jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                jsonString = objectMapper.writeValueAsString(object);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return jsonString;
    }

    public static Object json2Object(ObjectMapper objectMapper, String jsonString, Class<?> c) {
        if(jsonString != null && !"".equals(jsonString)) {
            try {
                return objectMapper.readValue(jsonString, c);
            } catch (Exception var4) {
                var4.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static Map<String, Object> jsonStrToMap(ObjectMapper objectMapper, String str) throws IOException {
        Map result = (Map)objectMapper.readValue(str, Map.class);
        return result;
    }

    public static List<Map<String, Object>> jsonStrToListMap(ObjectMapper objectMapper, String str) throws IOException {
        List result = (List)objectMapper.readValue(str, List.class);
        return result;
    }

    protected <X> List<X> changeJsonStrToList(ObjectMapper objectMapper, String str, Class<X> clazz) throws IOException {
        List result = (List)objectMapper.readValue(str, List.class);
        return result;
    }
}