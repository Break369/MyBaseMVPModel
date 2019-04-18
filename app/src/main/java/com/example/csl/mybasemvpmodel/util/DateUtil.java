package com.example.csl.mybasemvpmodel.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * 时间处理
 * Created by csl on 2017/5/12.
 */

public class DateUtil {

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();
        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        Log.e("时间轴",lt+"//"+ct+"///"+(cal.getTimeInMillis()-time.getTime())+"///"+(cal.getTimeInMillis()-time.getTime())/86400000);
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        }else {
            ftime = dateFormater2.get().format(time);
        }
            /* else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }*/
        return ftime;
    }

    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            String date = sdate.replace("T", " ");
            date = date.replace("Z", "");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            // Date date = formatter.parse(time);
            return dateFormater.get().parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toDateDay(String sdate) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDate(sdate));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            return year + "年" + (month + 1) + "月" + day + "日  "
                    + (hour < 10 ? "0" + hour : hour) + ":"
                    + (min < 10 ? "0" + min : min);
        } catch (Exception e) {
            return null;
        }
    }

    // 获取当前时间包括年月日时分秒
    public static String getNowDateTimeWhole() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return year + "-" + ((month + 1) < 10 ? ("0" + (month + 1)) : (month + 1)) + "-" + day + " "
                + (hour < 10 ? ("0" + hour) : hour) + ":"
                + (min < 10 ? ("0" + min) : min);
    }

    public static String getNowDay(int d) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.DAY_OF_MONTH, d);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return year + "-" + (month < 10 ? ("0" + month) : month) + "-"
                + (day < 10 ? ("0" + day) : day);
    }

    public static String getNowDay() {
        return getNowDay(0);
    }

    public static String getNowDay(int m, int d) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.DAY_OF_MONTH, d);
        calendar.add(Calendar.MONTH, m);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + (month < 10 ? ("0" + month) : month) + "-"
                + (day < 10 ? ("0" + day) : day);
    }

    public static String formatLongToTimeStr(int second) {
        int minute = 0;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        String strTime = (minute > 9 ? minute : "0" + minute) + "：" + (second > 9 ? second : "0" + second);
        return strTime;
    }


    public static int mYear;
    public static int mMonth;
    public static int mDay;

    /**
     * 显示时间窗口
     *
     * @param
     */
    /*public static void DateDialog(final Context mContext, final TextView textView) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.date_picker, null);
        final DatePicker dataPicker = v.findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dataPicker.init(mYear, mMonth, mDay, (datePicker, year, month, day) -> {
            mYear = year;
            mMonth = month;
            mDay = day;
        });

        new MaterialDialog.Builder(mContext)
                .customView(v, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    textView.setText(String.valueOf(mYear + "-" + (mMonth + 1) + "-" + mDay));
                }).show();
    }*/

    public static String getYM() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "_" + (month < 10 ? ("0" + month) : month);
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

    public static String getMD(String dateStr) {
        String newDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return (month < 10 ? ("0" + month) : month) + "/" + (day < 10 ? ("0" + day) : day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }



    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }


    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }


    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
}
