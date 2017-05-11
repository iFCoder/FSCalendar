/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */

/**
 * 文件名 : MonthViewPagerAdapter.java
 * 包含类名列表 : MonthViewPagerAdapter
 * 版本信息 : Ver 1.0
 * 创建日期 : 2016年05月22日 19:38
 */
package com.lf.li.fscalendar.view.itcalendar.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lf.li.fscalendar.view.itcalendar.view.WeekView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekViewPagerAdapter extends CalendarViewPagerAdapter {

    public WeekViewPagerAdapter(Context context, List<View> lists, int INIT_PAGER_INDEX, Calendar calendar,
                                ViewPager viewPager) {
        super(context, lists, INIT_PAGER_INDEX, calendar, viewPager);
    }


    @Override
    public void go2Date(Date date) {
        int targetPageIndex = getTargetPageIndex(date);
        mViewPager.setCurrentItem(targetPageIndex, false);
        chooseDate(date);
    }

    @Override
    public void go2DatePage(Date date) {
        int targetPageIndex = getTargetPageIndex(date);
        mViewPager.setCurrentItem(targetPageIndex, false);
    }

    private int getTargetPageIndex(Date date) {

        Calendar pre = Calendar.getInstance();
        pre.set(Calendar.YEAR, year);
        pre.set(Calendar.MONTH, month);
        pre.set(Calendar.DATE, day);
        pre.set(Calendar.HOUR_OF_DAY, 0);
        pre.set(Calendar.MINUTE, 0);
        pre.set(Calendar.SECOND, 0);
        pre.set(Calendar.MILLISECOND,0);
        int preIndexOfWeek = pre.get(Calendar.DAY_OF_WEEK);
        pre.add(Calendar.DATE, -preIndexOfWeek + 1); // 计算这周第一天的日期

        Calendar next = Calendar.getInstance();
        next.setTime(date);
        next.set(Calendar.HOUR_OF_DAY, 0);
        next.set(Calendar.MINUTE, 0);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND,0);
        int nextIndexOfWeek = next.get(Calendar.DAY_OF_WEEK);
        next.add(Calendar.DATE, -nextIndexOfWeek + 1);// 计算这周第一天的日期

        //这里有坑,next.getTimeInMillis() - pre.getTimeInMillis()是一周的时间,但是实际时间不是1000 * 3600 * 24 * 7
        //需要做处理
        long tag = next.getTimeInMillis() - pre.getTimeInMillis();
        long duration = Math.abs(tag);
        long first = duration / (1000 * 3600 * 24 * 7);
        long second = duration % (1000 * 3600 * 24 * 7);
        long gap = first + (second > 1000 * 3600 * 24 * 6 ? 1 : 0); //相差超过6天即为相差一周

        //需要判断index是否越界，一般不会
        return (int) (initPageIndex + (tag > 0 ? gap : -gap) );
    }

    @Override
    public void refreshView(View child, int jump) {
        WeekView weekView = (WeekView) child;
        weekView.refreshView(context, jump, year, month, day);
    }

    public Calendar getFirstDateBean(int position) {
        WeekView weekView = (WeekView) viewLists.get(position % count);
        WeekViewAdapter adapter = (WeekViewAdapter) weekView.getAdapter();
        Calendar c = Calendar.getInstance();
        if(adapter.getFirstDateBean() == null){
            return Calendar.getInstance();
        }
        c.setTime(adapter.getFirstDateBean().getDate());
        return c;
    }

}
