package com.common.utils;

import com.common.exception.FileOptionException;
import com.common.jdk.TdtObjectInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * description: 天气记录序列化和反序列化 <br>
 * date: 2019/10/25 17:08 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class ObjUtils {
    public static byte[] Object2Bytes(Object obj) throws IOException {
        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        ObjectOutputStream ooStream = new ObjectOutputStream(baoStream);
        ooStream.writeObject(obj);
        return baoStream.toByteArray();
    }

    public static Object Byte2Object(byte[] b) {
        Object obj = null;
        try (
                ByteArrayInputStream baiStream = new ByteArrayInputStream(b);
                TdtObjectInputStream oiStream = new TdtObjectInputStream(baiStream);
        ) {
            obj = oiStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new FileOptionException("Byte2Object转换失败", ex);
        }

        return obj;
    }

    public static byte[] blobToBytes(Blob blob) {
        BufferedInputStream is = null;
        byte[] bytes = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while (offset < len
                    && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }
        } catch (SQLException | IOException ex) {
            throw new FileOptionException("blobToBytes转换失败", ex);
        }
        return bytes;
    }

    private  final static char[] cs = "零一二三四五六七八九".toCharArray();


    /**
     *
     * @param numberString  含有数字的字符串
     * @return  字符串
     */
    public static String NumberToString(String numberString){
        String temp="";
        char[] ch = numberString.toCharArray();
        //遍历
        for (int i =0; i <ch.length ; i++) {
            if(Character.isDigit(ch[i])){
                temp+=cs[Integer.valueOf(String.valueOf(ch[i]))];
            }else {
                temp+=ch[i];
            }
        }
        return temp;

    }
}
