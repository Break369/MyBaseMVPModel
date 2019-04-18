package com.example.csl.mybasemvpmodel.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理
 * Created by liuyongfeng on 2017/5/12.
 */

public class StringUtil {

    /**
     * 对空字符串处理
     * @param s
     * @return
     */
    public static String getNotNullString(String s) {
        return getNotNullString(s, "");
    }

    /**
     * 对空字符串处理
     *
     * @param s
     * @param def
     * @return
     */
    public static String getNotNullString(String s, String def) {
        // String str = "";
        try {
            if (s == null || "".equals(s) || "null".equals(s)) {
                return def;
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取随机数
    public static String getRandomStr() {
        String s = "";
        Random ran = new Random(System.currentTimeMillis());
        for (int i = 0; i < 8; i++) {
            s = s + ran.nextInt(10);
        }
        return s;
    }

    // MD5加密
    public static String MD5(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 过滤特殊字符
    public static String StringFilter(String str) {

        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        try {
            str = str.replaceAll("\r", "").replaceAll("\n", "");
            String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？-]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}
