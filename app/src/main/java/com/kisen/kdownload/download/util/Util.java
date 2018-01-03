package com.kisen.kdownload.download.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by huangwy on 2017/12/27.
 * email: kisenhuang@163.com.
 */

public class Util {

    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encode(String url, String defaultName) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            digest.update(url.getBytes());
            // 获得密文
            byte[] md = digest.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return String.valueOf(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return defaultName;
        }
    }

    public static void checkNullString(String... str) {
        if (str == null || str.length == 0)
            return;
        for (String s : str) {
            if (TextUtils.isEmpty(s)) {
                throw new NullPointerException();
            }
        }
    }

}
