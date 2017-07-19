package com.suryani.manage.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    private static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    private static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_DATETIME_T = "dd/MM/yyyy'T'HH:mm:ss";

    public static Date parse(String date, String formatPattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern);
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String format(Date date, String formatPattern) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);
    }

    public static String formatTDate(String tDate) {
        try {
            SimpleDateFormat formatT = new SimpleDateFormat(DATE_FORMAT_DATETIME_T);
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DATETIME);
            Date date = formatT.parse(tDate);
            return format.format(date);
        } catch (ParseException e) {
            return tDate;
        }
    }

    public static Date getCurrentYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * get start datetime of the yesterday.
     *
     * @return
     */
    public static Date getCurrentYesterdayStartTime() {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return parse(format(cal.getTime(), DATE_FORMAT_DATE), DATE_FORMAT_DATE);

    }

    /**
     * get start datetime of the current day.
     *
     * @return
     */
    public static Date getCurrentDayStartTime() {
        return parse(format(new Date(), DATE_FORMAT_DATE), DATE_FORMAT_DATE);
    }

    /**
     * get end datetime of the current day.
     *
     * @return
     */
    public static Date getCurrentDayEndTime() {
        return parse(format(new Date(), DATE_FORMAT_DATE) + " 23:59:59", DATE_FORMAT_DATETIME);
    }

    /**
     * get first datetime of week, as sunday.
     *
     * @return
     */
    public static Date getCurrentWeekDayStartTime() {
        Calendar c = Calendar.getInstance();
        int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
        c.add(Calendar.DATE, -weekday);
        c.setTime(parse(format(c.getTime(), DATE_FORMAT_DATE) + " 00:00:00", DATE_FORMAT_DATETIME));
        return c.getTime();
    }

    /**
     * get last datetime of week,as saturday
     *
     * @return
     */
    public static Date getCurrentWeekDayEndTime() {
        Calendar c = Calendar.getInstance();
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DATE, 7 - weekday);
        c.setTime(parse(format(c.getTime(), DATE_FORMAT_DATE) + " 23:59:59", DATE_FORMAT_DATETIME));
        return c.getTime();
    }

    /**
     * get datetime of the current month
     *
     * @return
     */
    public static Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        return parse(format(c.getTime(), DATE_FORMAT_DATE), DATE_FORMAT_DATE);
    }

    /**
     * get end datetime of the current month.
     *
     * @return
     */
    public static Date getCurrentMonthEndTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        return parse(format(c.getTime(), DATE_FORMAT_DATE) + " 23:59:59", DATE_FORMAT_DATETIME);
    }

    /**
     * get start datetime of the current quarter.
     *
     * @return
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3)
            c.set(Calendar.MONTH, 0);
        else if (currentMonth >= 4 && currentMonth <= 6)
            c.set(Calendar.MONTH, 3);
        else if (currentMonth >= 7 && currentMonth <= 9)
            c.set(Calendar.MONTH, 6);
        else if (currentMonth >= 10 && currentMonth <= 12)
            c.set(Calendar.MONTH, 9);
        c.set(Calendar.DATE, 1);
        return parse(format(c.getTime(), DATE_FORMAT_DATE) + " 00:00:00", DATE_FORMAT_DATETIME);
    }

    /**
     * get the end datetime of the current quarter.
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 2);
            c.set(Calendar.DATE, 31);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 5);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 8);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
        }
        return parse(format(c.getTime(), DATE_FORMAT_DATE) + " 23:59:59", DATE_FORMAT_DATETIME);
    }

    /**
     * get start datetime of the current year.
     */
    public static Date getCurrentYearStartTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        return parse(format(c.getTime(), DATE_FORMAT_DATE), DATE_FORMAT_DATE);
    }

    /**
     * get end datetime of the current year.
     *
     * @return
     */
    public static Date getCurrentYearEndTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DATE, 31);
        return parse(format(c.getTime(), DATE_FORMAT_DATE) + " 23:59:59", DATE_FORMAT_DATETIME);
    }

    public static String getTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return dateFormat.format(new Date());

    }

    /**
     * 当前日期之后的几天的日期
     *
     * @return
     */
    public static String getCurrentAfterStartTime(int days) {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return format(cal.getTime(), DATE_FORMAT_DATE);

    }

    public static Date getDaysDate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return parse(format(c.getTime(), DATE_FORMAT_DATETIME), DATE_FORMAT_DATETIME);
    }

    public static void main(String[] args) {
        System.out.println(getCurrentAfterStartTime(1));
    }

}