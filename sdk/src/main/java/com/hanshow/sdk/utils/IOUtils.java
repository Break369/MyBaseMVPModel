package com.hanshow.sdk.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by mfw on 2018/4/2.
 * <p>
 * IO流工具类
 */
public class IOUtils {
    /**
     * 关闭流
     */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return true;
    }
}
