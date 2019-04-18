package com.hanshow.sdk.utils;

import java.security.MessageDigest;

/**
 * Created by pc on 2018/8/23.
 */

public class MD5Util {

        static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

        public static String byteArrayToHexString(byte b[]) {
            StringBuffer resultSb = new StringBuffer();
            for (int i = 0; i < b.length; i++)
                resultSb.append(byteToHexString(b[i]));

            return resultSb.toString();
        }

        public static String byteToHexString(byte b) {
            int n = b;
            if (n < 0)
                n += 256;
            int d1 = n / 16;
            int d2 = n % 16;
            return hexDigits[d1] + hexDigits[d2];
        }

        /**
         * MD5加密
         *
         * @param origin 加密字符串
         * @param charset 字符编码 可为null
         * @return
         */
        public static String MD5Encode(String origin, String charset) {
            String resultString = null;
            try {
                resultString = new String(origin);
                MessageDigest md = MessageDigest.getInstance("MD5");
                if (charset == null)
                    resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
                else
                    resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
            } catch (Exception exception) {
            }
            return resultString;
        }

        /*
       * MD5加密，32位
       */
        public static String getMD5(String str) {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            char[] charArray = str.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        }

        /**
         * 32位 MD5加密结果
         * @param source 需要加密的原字符串
         * @param encoding 指定编码类型
         * @param uppercase 是否转为大写字符串
         * @return 加密结果
         * @since 1.0
         */
        public static String encode_32(String source, String encoding, boolean uppercase){
            return MD5Encode(source, encoding, uppercase);
        }

        /**
         * MD5加密
         * @param source 需要加密的原字符串
         * @param encoding 指定编码类型
         * @param uppercase 是否转为大写字符串
         * @return 加密结果
         * @since 1.0
         */
        private static String MD5Encode(String source, String encoding, boolean uppercase) {
            String result = null;
            try {
                result = source;
                // 获得MD5摘要对象
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组更新摘要信息
                messageDigest.update(result.getBytes(encoding));
                // messageDigest.digest()获得16位长度
                result = byteArrayToHexString(messageDigest.digest());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return uppercase ? result.toUpperCase() : result;
        }

    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
}
