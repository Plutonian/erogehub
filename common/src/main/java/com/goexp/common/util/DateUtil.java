package com.goexp.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    public static String formatDate(LocalDate date) {
        if (date == null)
            return "";
        var now = LocalDate.now();

        var days = date.toEpochDay() - now.toEpochDay();

        if (days == 0) {
            return "今日";
        }

        if (days == -1) {
            return "昨日";
        }
        if (days == 1) {
            return "明日";
        }

        if (days > 0) {
            return String.format("あと%d日", days);
        }

        if (days > -8) {
            return String.format("%d日前", -days);
        }

        return date.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
    }

    public static boolean needFormat(LocalDate date) {
        if (date == null)
            return false;
        var now = LocalDate.now();

        var days = date.toEpochDay() - now.toEpochDay();

        return days > -8;

    }

    public static Date toDate(String dateToConvert) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return format.parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static LocalDate toLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

}
