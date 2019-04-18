package com.hanshow.sdk.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Dell on 2018/4/16.
 */

public class DeviceUtils {

    /**
     * 获取手机IMEI号
     *
     * 需要动态权限: android.permission.READ_PHONE_STATE
     */
    public static String getIMEI(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return ANDROID_ID;
    }
}
