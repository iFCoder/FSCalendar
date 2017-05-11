/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.lf.li.fscalendar.view.itcalendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lf.li.fscalendar.view.itcalendar.utils.DateBean;
import com.lf.li.fscalendar.view.itcalendar.utils.SpecialCalendar;
import com.lf.li.fscalendar.view.itcalendar.view.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class CalendarViewPagerAdapter extends PagerAdapter {

    protected List<View> viewLists;
    protected int count = 3;
    protected Context context;

    protected int year;
    protected int month;
    protected int day;

    protected ViewPager mViewPager;

    /**
     *  初始化页下标
     */
    protected int initPageIndex;

    public CalendarViewPagerAdapter(Context context, List<View> lists, int INIT_PAGER_INDEX, Calendar calendar, ViewPager viewPager) {
        this.context = context;
        viewLists = lists;
        count = lists.size();
        initPageIndex = INIT_PAGER_INDEX;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        mViewPager = viewPager;
    }



    @Override
    public int getCount() {                                                                 //获得size
        return initPageIndex * 2 + 1;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View child = viewLists.get(position % count);
        refreshView(child, position - initPageIndex);
        if (child != null) {
            if(container.getChildCount() < 3){
                container.addView(child);
            }
        }
        return child;
    }

    public abstract void go2Date(Date date);
    public abstract void go2DatePage(Date date);
    public abstract void refreshView(View child, int jump);

    public void chooseDate(Date date){
        for(int i=0;i<3;++i){
            DateBean[] dateBeans = ((CalendarAdapter)((CalendarView)viewLists.get(i)).getAdapter()).getDateBeans();
            for(DateBean d : dateBeans){
                if(SpecialCalendar.isSameDay(date,d.getDate())){
                    d.setIsChoosed(true);
                }else {
                    d.setIsChoosed(false);
                }
            }
            ((CalendarAdapter)((CalendarView)viewLists.get(i)).getAdapter()).notifyDataSetChanged();
        }
    }

    public void clearPage(int position){
        DateBean[] dateBeans = ((CalendarAdapter)((CalendarView)viewLists.get(mViewPager.getCurrentItem() % 3))
                .getAdapter()).getDateBeans();
        for(DateBean d : dateBeans){
            d.setIsChoosed(false);
        }
        ((CalendarAdapter)((CalendarView)viewLists.get(mViewPager.getCurrentItem() % 3)).getAdapter()).notifyDataSetChanged();
    }

    public void refreshPage(int position,Date choosedDate){
        DateBean[] dateBeans = ((CalendarAdapter)((CalendarView)viewLists.get(position % 3))
                .getAdapter()).getDateBeans();
        for(DateBean d : dateBeans){
            if(SpecialCalendar.isSameDay(choosedDate,d.getDate())){
                d.setIsChoosed(true);
            }else {
                d.setIsChoosed(false);
            }
        }
        ((CalendarAdapter)((CalendarView)viewLists.get(position % 3)).getAdapter()).notifyDataSetChanged();
    }

    public void updateChoosedDate(DateBean dateBean){
        Calendar c = Calendar.getInstance();
        c.setTime(dateBean.getDate());
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
    }

    public void updateEvents(List<Long> eventDates){
        if(eventDates == null)
        {
            eventDates = new ArrayList<>();
        }
        for(View view : viewLists){
            ((CalendarAdapter)((CalendarView)view).getAdapter()).updateEvents(eventDates);
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        ((CalendarView) viewLists.get(mViewPager.getCurrentItem() % 3)).refreshSelf();
    }

}