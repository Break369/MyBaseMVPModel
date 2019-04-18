package com.hanshow.sdk.helper.okhttp;


import android.util.Log;

import com.hanshow.sdk.utils.AppUtils;

import java.io.File;

import okhttp3.Cache;

/**
 * Created by mfw on 2018/4/2.
 * <p>
 */
public class HttpCache {

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 50 * 1024 * 1024;

    public static Cache getCache() {
        return new Cache(new File(AppUtils.getContext().getExternalCacheDir().getAbsolutePath() + File
                .separator + "data/NetCache"),
                HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }
}
