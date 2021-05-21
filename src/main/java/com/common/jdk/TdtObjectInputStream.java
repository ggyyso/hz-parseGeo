package com.common.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * description: 反序列化天气对象时替换指定类 <br>
 * date: 2019/10/25 17:12 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class TdtObjectInputStream extends ObjectInputStream {
    protected TdtObjectInputStream() throws IOException, SecurityException {
        super();
    }

    public TdtObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        try {
            if (name.startsWith("com.sxgis.model.weather")) {
                name = name.replace("com.sxgis.model.weather"
                        , "com.sxgis.hzcom.dto.map");
            }
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return super.resolveClass(desc);
    }
}
