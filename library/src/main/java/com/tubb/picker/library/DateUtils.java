package com.tubb.picker.library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bingbing.tu
 * 2015/8/14.
 */
public class DateUtils {

    public static String[] weekName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH) + 1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;

    }

    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : ("0" + month))
                + "-01";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    public static String formatDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(c.getTime());
    }

    public static Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getUpDownYears(int delay) {
        List<String> years = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);

        for (int up = delay; up > 0; up--) {
            years.add(String.format("%d年", cYear - up));
        }
        years.add(String.format("%d年", cYear));

        for (int down = 1; down <= delay; down++) {
            years.add(String.format("%d年", cYear + down));
        }
        return years;
    }

    public static List<String> getUpYears(int delay) {
        List<String> years = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);

        for (int up = delay; up > 0; up--) {
            years.add(String.format("%d年", cYear - up));
        }
        years.add(String.format("%d年", cYear));
        return years;
    }

    public static List<String> getDownYears(int delay) {

        List<String> years = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        years.add(String.format("%d年", cYear));
        for (int down = 1; down <= delay; down++) {
            years.add(String.format("%d年", cYear + down));
        }
        return years;
    }

    public static List<String> getMonths() {
        List<String> months = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            months.add(String.format("%d月", month));
        }
        return months;
    }

    public static List<String> getDayList(int year, int month) {
        int days = getMonthDays(year, month);
        List<String> dayList = new ArrayList<>();
        for (int day = 1; day <= days; day++) {
            dayList.add(String.format("%d日", day));
        }
        return dayList;
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

}
