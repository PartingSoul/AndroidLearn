package com.parting_soul.support.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author parting_soul
 * @date 2019/4/23
 */
public class DateUtils {


    public static String parseTimeToString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            return sdf.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseTimeToString(String time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        SimpleDateFormat outputSdf = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            return outputSdf.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String parseMillisecondToString(long milliseconds, String pattern) {
        Date date = new Date(milliseconds);
        SimpleDateFormat outputSdf = new SimpleDateFormat(pattern, Locale.CHINA);

        return outputSdf.format(date);
    }
}
