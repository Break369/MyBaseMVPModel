package com.example.csl.mybasemvpmodel.global;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.hanshow.sdk.global.GlobalApplication;
import com.tencent.bugly.Bugly;

import java.util.List;

/**
 * Created by mfw on 2018/4/3.
 * <p>
 */

public class MyApplication extends GlobalApplication {

    // user your appid the key.
    private static final String APP_ID = "2882303761517850256";
    // user your appid the key.
    private static final String APP_KEY = "5731785072256";

    /*private static DemoHandler sHandler = null;
    private static MainActivity sMainActivity = null;*/

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;
    public static MyApplication app;
    public static final String TAG = "com.hanshow.easychain";
    public static String easy = "0.00";
    public static String eth = "0.00";
    public static Context context;



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static Context getContext() {
        if (context == null) {
            context = new MyApplication();
        }
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        app = this;
        //初始化屏幕宽高
        getScreenSize();
        /*if (shouldInit()) {
            try {
                MiPushClient.registerPush(this, APP_ID, APP_KEY);
            }catch (Exception e){
            }
        }
        MobSDK.init(this);*/
        //CrashReport.initCrashReport(getApplicationContext(), "51e6cf2e5a", false);
        Bugly.init(getApplicationContext(), "51e6cf2e5a", false);
        //百度地图初始化
        /*SDKInitializer.initialize(getApplicationContext());

        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }
            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
        if (sHandler == null) {
            sHandler = new DemoHandler(getApplicationContext());
        }*/
    }

    /*小米推送*/
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
    /*public static DemoHandler getHandler()   {
        return sHandler;
    }

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    public static class DemoHandler extends Handler {

        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            if (sMainActivity != null) {
                sMainActivity.refreshLogInfo();
            }
            if (!TextUtils.isEmpty(s)) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                Log.i("handleMessage", "handleMessage: " + s);
            }

        }
    }*/

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }
}
