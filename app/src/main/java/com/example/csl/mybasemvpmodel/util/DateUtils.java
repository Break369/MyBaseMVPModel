package com.example.csl.mybasemvpmodel.util;

import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mfw on 2018/4/2.
 * <p>
 * 日期时间工具类
 */
public class DateUtils {

    public static final long ONE_SECOND_MILLIONS = 1000;
    public static final long ONE_MINUTE_MILLIONS = 60 * ONE_SECOND_MILLIONS;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;
    public static final int DAY_OF_YEAR = 365;

    // 日期格式为 2016-02-03 17:04:58
    public static final String PATTERN_DATE = "yyyy年MM月dd日";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_SPLIT = " ";
    public static final String PATTERN = PATTERN_DATE + PATTERN_SPLIT + PATTERN_TIME;

    public static String getShortTime(String dateStr) {
        String str;

        Date date = str2date(dateStr);
        Date curDate = new Date();

        long durTime = curDate.getTime() - date.getTime();
        int dayDiff = calculateDayDiff(date, curDate);

        if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
            str = "刚刚";
        } else if (durTime < ONE_HOUR_MILLIONS) {
            str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
        } else if (dayDiff == 0) {
            str = durTime / ONE_HOUR_MILLIONS + "小时前";
        } else if (dayDiff == -1) {
            str = "昨天" + DateFormat.format("HH:mm", date);
        } else if (isSameYear(date, curDate) && dayDiff < -1) {
            str = DateFormat.format("MM-dd", date).toString();
        } else {
            str = DateFormat.format("yyyy-MM", date).toString();
        }

        return str;
    }
    public static String getShortTimeLong(String time) {
        String str;
        Date date = new Date(Long.parseLong(time));
        Date curDate = new Date();
        long durTime = curDate.getTime() - date.getTime();
        int dayDiff = calculateDayDiff(date, curDate);

        if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
            str = "刚刚";
        } else if (durTime < ONE_HOUR_MILLIONS) {
            str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
        } else if (dayDiff == 0) {
            str = durTime / ONE_HOUR_MILLIONS + "小时前";
        } else if (dayDiff == -1) {
            str = "昨天" + DateFormat.format("HH:mm", date);
        } else if (isSameYear(date, curDate) && dayDiff < -1) {
            str = DateFormat.format(PATTERN, date).toString();
        } else {
            str = DateFormat.format(PATTERN, date).toString();
        }

        return str;
    }

    /**
     * 获取日期 PATTERN_DATE 部分
     */
    public static String getDate(String date) {
        if (TextUtils.isEmpty(date) || !date.contains(PATTERN_SPLIT)) {
            return "";
        }
        return date.split(PATTERN_SPLIT)[0];
    }

    /**
     * 原有日期上累加月
     *
     * @return 累加后的日期 PATTERN_DATE 部分
     */
    public static String addMonth(String date, int moonCount) {
        //如果date为空 就用当前时间
        if (TextUtils.isEmpty(date)) {
            SimpleDateFormat df = new SimpleDateFormat(PATTERN_DATE + PATTERN_SPLIT + PATTERN_TIME);
            date = df.format(new Date());
        }
        Calendar calendar = str2calendar(date);
        calendar.add(Calendar.MONTH, moonCount);
        return getDate(calendar2str(calendar));
    }

    /**
     * 计算天数差
     */
    public static int calculateDayDiff(Date targetTime, Date compareTime) {
        boolean sameYear = isSameYear(targetTime, compareTime);
        if (sameYear) {
            return calculateDayDiffOfSameYear(targetTime, compareTime);
        } else {
            int dayDiff = 0;

            // 累计年数差的整年天数
            int yearDiff = calculateYearDiff(targetTime, compareTime);
            dayDiff += yearDiff * DAY_OF_YEAR;

            // 累计同一年内的天数
            dayDiff += calculateDayDiffOfSameYear(targetTime, compareTime);

            return dayDiff;
        }
    }

    /**
     * 计算同一年内的天数差
     */
    public static int calculateDayDiffOfSameYear(Date targetTime, Date compareTime) {
        if (targetTime == null || compareTime == null) {
            return 0;
        }

        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }

    /**
     * 计算年数差
     */
    public static int calculateYearDiff(Date targetTime, Date compareTime) {
        if (targetTime == null || compareTime == null) {
            return 0;
        }

        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear - comYear;
    }

    /**
     * 计算月数差
     *
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static int calculateMonthDiff(String targetTime, String compareTime) {
        return calculateMonthDiff(str2date(targetTime, PATTERN_DATE),
                str2date(compareTime, PATTERN_DATE));
    }

    /**
     * 计算月数差
     *
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static int calculateMonthDiff(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);
        int tarMonth = tarCalendar.get(Calendar.MONTH);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);
        int comMonth = compareCalendar.get(Calendar.MONTH);
        return ((tarYear - comYear) * 12 + tarMonth - comMonth);

    }

    /**
     * 是否为同一年
     */
    public static boolean isSameYear(Date targetTime, Date compareTime) {
        if (targetTime == null || compareTime == null) {
            return false;
        }

        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }

    public static Date str2date(String str, String format) {
        Date date = null;
        try {
            if (str != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                date = sdf.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date str2date(String str) {
        return str2date(str, PATTERN);
    }

    public static String date2str(Date date) {
        return date2str(date, PATTERN);
    }

    public static String date2str(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    public static Calendar str2calendar(String str) {
        Calendar calendar = null;
        Date date = str2date(str);
        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }
        return calendar;
    }


    public static Calendar str2calendar(String str, String format) {
        Calendar calendar = null;
        Date date = str2date(str, format);
        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }
        return calendar;
    }

    public static String calendar2str(Calendar calendar) {
        return date2str(calendar.getTime());
    }

    public static String calendar2str(Calendar calendar, String format) {
        return date2str(calendar.getTime(), format);
    }



    /**
     * 转换时间日期格式字串为long型
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }

    }

    /**
     * 转换时间日期格式字串为long型
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static String convertTimeToString(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String times = formatter.format(new Date(time));
        return times;
    }

    /**
     * 转换时间日期格式字串为long型
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static String convertTimeToStringS(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String times = formatter.format(new Date(time));
        return times;
    }
    /**
     * 转换时间日期格式字串为long型
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static String convertTimeToString2(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String times = formatter.format(new Date(time));
        return times;
    }

    /**
     * 判断是否同一天
     * @param currentTime
     * @param lastTime
     * @return
     */
    public static boolean isSameData(String currentTime, String lastTime) {
        if(convertTimeToStringS(Long.parseLong(lastTime)).equals(convertTimeToStringS(Long.parseLong(currentTime)))){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(cal1 != null && cal2 != null) {
            return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }
    }


    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = format.format(today);
        return result;
    }

    /**
     * 算时间差
     * @param startTime
     * @return
     */
    public static String getDatePoor(Long startTime) {
        Calendar calendar = Calendar.getInstance();
        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
        long nh = 1000 * 60 * 60;//每小时毫秒数
        long nm = 1000 * 60;//每分钟毫秒数
        long diff = startTime - calendar.getTimeInMillis(); // 获得两个时间的毫秒时间差异
        long day = diff / nd;   // 计算差多少天
        long hour = diff % nd / nh; // 计算差多少小时
        long min = diff % nd % nh / nm;  // 计算差多少分钟
        return day + "天" + hour + "小时" + min + "分钟";
    }


}
