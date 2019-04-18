package com.hanshow.sdk.utils;



import android.util.Base64;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 签名工具类
 */
public class EncryptUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/NoPadding";

    /**
     * MD5签名算法
     * 按字首排序加key后MD5
     *
     * @param o   要参与签名的数据对象
     * @param key 加密key
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getMd5Sign(Object o, String key) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<>();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && !"".equals(f.get(o))) {
                list.add(f.getName() + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        result = MD5Util.MD5Encode(result, null).toUpperCase();
        return result;
    }

    /**
     * MD5签名算法
     * 按字首排序加key后MD5
     *
     * @param map 要参与签名的数据字典
     * @param key 加密key
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getMd5Sign(Map<String, Object> map, String key) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null && !"".equals(entry.getValue())) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        LogUtils.i(result);
        result = MD5Util.MD5Encode(result, null).toUpperCase();
        return result;
    }



    /**
     * 商品查询获取加密key
     * @param merchantCode
     * @param slat
     * @param serviceName
     * @param timestamp
     * @param json
     * @return
     */
    public static String getOrderSign(String merchantCode,String slat,String serviceName,Long timestamp,String json){
        StringBuilder basestring = new StringBuilder();
        basestring.append("merchantCode=").append(merchantCode).append("&serviceName=")
                .append(serviceName).append("&timestamp=").append(timestamp);
        if(json.length()>0){
            basestring.append("&").append(json);
        }
        LogUtils.i(basestring.toString());
        return MD5Util.encode_32(basestring+slat, "UTF-8", true);
    }


    /**
     * 商品查询获取加密key
     * @param merchantId
     * @param storeId
     * @param orderNo
     * @param key
     * @return
     */
    public static String getOrderSign(String merchantId,String storeId,String orderNo,String key){
        StringBuilder basestring = new StringBuilder();
        basestring.append("merchantID=").append(merchantId).append("&orderNo=")
                .append(orderNo).append("&signType=MD5").append("&storeID=")
                .append(storeId).append("&key=").append(key);
        LogUtils.i(basestring.toString());
        return MD5Util.MD5Encode(basestring.toString(),"UTF-8").toUpperCase();
    }

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encrypt(String content, String password)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.getBytes("UTF-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0)
            {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
            IvParameterSpec ivspec = new IvParameterSpec(password.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            byte[] msgBytes = Base64.encode(encrypted,Base64.DEFAULT);
            return new String(msgBytes);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decrpyt(String content, String password)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = Base64.decode(content.getBytes(), Base64.DEFAULT);
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0)
            {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
            IvParameterSpec ivspec = new IvParameterSpec(password.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return new String(encrypted, "UTF-8");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
