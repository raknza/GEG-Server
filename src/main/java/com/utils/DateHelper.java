package com.utils;

import java.util.Calendar;
import java.util.Date;

public final class DateHelper {

    private static final Calendar calendar = Calendar.getInstance();

    public static String getNowTime(){
        Date nowDate = new Date();
        calendar.setTime(nowDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String nowTime = (year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        return nowTime;
    }
}
