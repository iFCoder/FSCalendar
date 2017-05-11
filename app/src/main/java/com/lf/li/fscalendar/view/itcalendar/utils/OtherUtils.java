/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */

package com.lf.li.fscalendar.view.itcalendar.utils;

import android.content.Context;
import android.util.TypedValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OtherUtils {

    public static final Calendar calendar = Calendar.getInstance();

    /**
     * 格式(yyyy-MM-dd)
     */
    public static final String DATE_PATTERN_1 = "yyyy年MM月dd日";
    /**
     * 格式(yyyy-MM)
     */
    public static final String DATE_PATTERN_2 = "yyyy年MM月";

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    /**
     * 根据指定的格式对日期进行格式化处理
     *
     * @param date 日期
     * @return 日期
     */
    public static String formatMonth(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern(DATE_PATTERN_2);
        return df.format(date);
    }

    /**
     * 根据指定的格式对日期进行格式化处理
     *
     * @param date 日期
     * @return 日期
     */
    public static String formatDate(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern(DATE_PATTERN_2);
        return df.format(date);
    }


    /**
     * 根据指定的格式对日期进行格式化处理
     *
     * @param date   日期
     * @param format 格式
     * @return 日期
     */
    public static String formatDate(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern(format);
        return df.format(date);
    }

    public static float dp2px(Context context, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}
