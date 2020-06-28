package com.yqh.falcon.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date addHour(Date date,int hour){
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(date);
           calendar.add(Calendar.HOUR,hour);
           date = calendar.getTime();
           return date;
    }

    public static Date addMonth(Date date,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,month);
        return calendar.getTime();
    }
}
