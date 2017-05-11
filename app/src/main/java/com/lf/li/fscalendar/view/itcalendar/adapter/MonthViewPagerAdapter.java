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

import com.lf.li.fscalendar.view.itcalendar.view.MonthView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthViewPagerAdapter extends CalendarViewPagerAdapter {

    public MonthViewPagerAdapter(Context context, List<View> lists, int INIT_PAGER_INDEX, Calendar calendar,
                                 ViewPager viewPager) {
        super(context, lists, INIT_PAGER_INDEX, calendar, viewPager);
    }

    @Override
    public void go2Date(Date date) {
        int targetPageIndex = getTargetPageIndex(date);
        mViewPager.setCurrentItem(targetPageIndex,false);
        chooseDate(date);
    }

    @Override
    public void go2DatePage(Date date) {
        int targetPageIndex = getTargetPageIndex(date);
        mViewPager.setCurrentItem(targetPageIndex,false);
    }

    @Override
    public void refreshView(View child, int jump) {
        MonthView monthView = (MonthView) child;
        monthView.refreshView(context, jump, year, month, day);
    }

    private int getTargetPageIndex(Date date) {

        Calendar next = Calendar.getInstance();
        next.setTime(date);
        int nextYear = next.get(Calendar.YEAR);
        int nextMonth = next.get(Calendar.MONTH);

        //需要判断index是否越界，一般不会
        int offsetPages = nextMonth - month + (nextYear - year) * 12;
        return initPageIndex + offsetPages;
    }

    public Calendar getFirstDateBean(int position) {
        MonthView monthView = (MonthView) viewLists.get(position % count);
        MonthViewAdapter adapter = (MonthViewAdapter) monthView.getAdapter();
        Calendar c = Calendar.getInstance();
        if(adapter.getFirstDateBean() == null){
            return Calendar.getInstance();
        }
        c.setTime(adapter.getFirstDateBean().getDate());
        return c;
    }
}
