package com.xbtz.ilikenow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ilikenow on 2015/10/19.
 * 将字节流转换为字符串的工具类
 */
public class HttpUtils {
    public static String readMyInputStream(InputStream is) {
        byte[] result;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer))!=-1) {
                baos.write(buffer,0,len);
            }
            is.close();
            baos.close();
            result = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return "获取数据失败。";
        }
        return new String(result);
    }
}
