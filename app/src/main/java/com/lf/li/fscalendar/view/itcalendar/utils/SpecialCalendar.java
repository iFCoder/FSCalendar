/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.lf.li.fscalendar.view.itcalendar.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SpecialCalendar {

    public static final int CALENDAR_ITEM_SIZE = 42;
    public static final int CALENDAR_WEEK_SIZE = 7;

    /**
     * 获取隔某天n周的周数据
     *
     * @param jumpWeek
     * @param year
     * @param month
     * @param day
     *
     * @return
     */
    public static DateBean[] getWeekDate(int jumpWeek, int year, int month, int day) {
        int[] dates = getRealDate(jumpWeek, year, month, day);
        return getWeekDate(dates[0], dates[1], dates[2]);
    }

    /**
     * 获取某天所在的周日期数据
     *
     * @param year
     * @param month
     * @param day
     *
     * @return
     */
    public static DateBean[] getWeekDate(int year, int month, int day) {
        DateBean[] dateBeens = new DateBean[CALENDAR_WEEK_SIZE];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        //当前天是周几
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < 7; ++i) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DATE, day - dayOfWeek + 1 + i);
            DateBean dateBean = new DateBean();

            if (day - dayOfWeek < 0) {
                dateBean.setMonthType(DateBean.LAST_MONTH);
            } else if (c.get(Calendar.MONTH) == month) {
                dateBean.setMonthType(DateBean.CURRENT_MONTH);
            } else {
                dateBean.setMonthType(DateBean.NEXT_MONTH);
            }
            Calendar calCurrent = Calendar.getInstance();
            if (c.get(Calendar.YEAR) == calCurrent.get(Calendar.YEAR) && c.get(Calendar.MONTH) == calCurrent
                    .get(Calendar
                            .MONTH) && c.get(Calendar.DAY_OF_MONTH) == calCurrent.get(Calendar.DAY_OF_MONTH)) {
                dateBean.setIsCurrentDay(true);
                dateBean.setIsChoosed(true);
            }
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            dateBean.setDate(c.getTime());
            dateBean.setPositionOfMonthPage(i);
            dateBean.setPositionOfWeekPage(c.get(Calendar.DAY_OF_WEEK) - 1);
            dateBean.setWeekOfMonthPage(c.get(Calendar.WEEK_OF_MONTH) - 1);
            dateBeens[i] = dateBean;
        }
        return dateBeens;
    }

    /**
     * 获取某月的日期数据
     *
     * @param jumpMonth 距基准月n个月的数据
     * @param year
     * @param month
     *
     * @return
     */
    public static DateBean[] getMonthDate(int jumpMonth, int year, int month) {
        Calendar calCurrent = Calendar.getInstance();
        int[] realYearMonth = getRealDate(jumpMonth, year, month);
        year = realYearMonth[0];
        month = realYearMonth[1];

        DateBean[] dateBeans = new DateBean[CALENDAR_ITEM_SIZE];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 1);
        // 本月总天数
        int daySizeThisMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 本月第一天是周几
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int weekdayOfMonth = cal.get(Calendar.DAY_OF_WEEK) - 1;
        // 上个月总天数
        cal.set(Calendar.MONTH, month - 1);
        int daySizeLastMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int lastMonthDay = 1;
        for (int i = 0; i < CALENDAR_ITEM_SIZE; i++) {
            DateBean dateBean = new DateBean();
            if (i < weekdayOfMonth) {
                // 前一个月
                int day = daySizeLastMonth - weekdayOfMonth + i + 1;
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, day);
                dateBean.setMonthType(DateBean.LAST_MONTH);
            } else if (i < daySizeThisMonth + weekdayOfMonth) {
                // 本月
                int day = i - weekdayOfMonth + 1;
                if (year == calCurrent.get(Calendar.YEAR) && month == calCurrent.get(Calendar.MONTH)
                        && day == calCurrent.get(Calendar.DAY_OF_MONTH)) {
                    dateBean.setIsCurrentDay(true);
                    dateBean.setIsChoosed(true);
                }
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                dateBean.setMonthType(DateBean.CURRENT_MONTH);
            } else {
                // 下一个月
                cal.set(year, month, lastMonthDay);
                cal.add(Calendar.MONTH, 1);
                lastMonthDay++;
                dateBean.setMonthType(DateBean.NEXT_MONTH);
            }
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            dateBean.setDate(cal.getTime());
            dateBean.setPositionOfMonthPage(i);
            dateBean.setPositionOfWeekPage(cal.get(Calendar.DAY_OF_WEEK) - 1);
            dateBean.setWeekOfMonthPage(cal.get(Calendar.WEEK_OF_MONTH) - 1);
            dateBeans[i] = dateBean;
        }

        return dateBeans;
    }

    /**
     * 计算距基准月1号n个月的日期
     *
     * @param jumpMonth
     * @param year
     * @param month
     *
     * @return
     */
    private static int[] getRealDate(int jumpMonth, int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, jumpMonth);
        return new int[] {calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)};
    }

    /**
     * 计算距基准月某天n个月的日期
     *
     * @param jumpWeek
     * @param year
     * @param month
     * @param day
     *
     * @return
     */
    private static int[] getRealDate(int jumpWeek, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day + jumpWeek * 7);
        return new int[] {calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)};
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        int y1 = c1.get(Calendar.YEAR);
        int m1 = c1.get(Calendar.MONTH);
        int d1 = c1.get(Calendar.DATE);

        int y2 = c2.get(Calendar.YEAR);
        int m2 = c2.get(Calendar.MONTH);
        int d2 = c2.get(Calendar.DATE);

        if (y1 == y2 && m1 == m2 && d1 == d2) {
            return true;
        }

        return false;
    }

    /**
     * 匹配有时间的日期
     *
     * @param eventDates
     * @param dateBeans
     */
    public static void updateEvents(List<Long> eventDates, DateBean[] dateBeans) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //清除状态
        for (DateBean dateBean : dateBeans) {
            dateBean.setHasEvent(false);
        }
        for (long date : eventDates) {
            for (DateBean dateBean : dateBeans) {
                if (TextUtils.equals(format.format(new Date(date)), format.format(dateBean.getDate()))) {
                    dateBean.setHasEvent(true);
                }
            }
        }
    }

    public static Calendar getMonthStartTime(Calendar calendar) {
        DateBean[] dateBeens = SpecialCalendar.getMonthDate(0,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
        DateBean start = dateBeens[0];
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(start.getDate());
        //时分秒清零
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime;
    }

    public static Calendar getMonthEndTime(Calendar calendar) {
        DateBean[] dateBeens = SpecialCalendar.getMonthDate(0,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
        DateBean end = dateBeens[dateBeens.length-1];
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(end.getDate());
        //时分秒清零
        endTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        return endTime;
    }

    public static boolean containCalendar(List<Calendar> datas, Calendar calendar) {
        boolean result = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        for (Calendar c : datas) {
            if (TextUtils.equals(format.format(c.getTime()), format.format(calendar.getTime()))) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean isCurrMonth(Date date) {
        Calendar cur = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (cur.get(Calendar.YEAR) == c.get(Calendar.YEAR) && cur.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
            return true;
        }
        return false;
    }

    public static boolean isCurrWeek(Date date) {
        Calendar cur = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (cur.get(Calendar.YEAR) == c.get(Calendar.YEAR) && cur.get(Calendar.MONTH) == c.get(Calendar.MONTH)
                && cur.get(Calendar.WEEK_OF_MONTH) == c.get(Calendar.WEEK_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public enum CalendarType{
        WEEK,MONTH
    }

    public static int getWeekOfMonthPage(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_MONTH) - 1;
    }

    public static boolean needChangeMonth(Date d1,Date d2){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)){
            return false;
        }
        return true;
    }

    public static boolean needChangeWeek(Date d1,Date d2){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1
                .get(Calendar.WEEK_OF_MONTH) == c2.get(Calendar.WEEK_OF_MONTH)){
            return false;
        }
        return true;
    }

    public static Date deepCopyDate(Date date){
        Date result = new Date();
        result.setTime(date.getTime());
        return result;
    }
}