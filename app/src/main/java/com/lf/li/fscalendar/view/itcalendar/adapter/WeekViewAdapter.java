/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.lf.li.fscalendar.view.itcalendar.adapter;

import android.content.Context;

import com.lf.li.fscalendar.R;
import com.lf.li.fscalendar.view.itcalendar.utils.DateBean;
import com.lf.li.fscalendar.view.itcalendar.utils.SpecialCalendar;

import java.util.Calendar;


public class WeekViewAdapter extends CalendarAdapter {
    private DateBean firstDateBean;
    private int firstDatePosition;

    public WeekViewAdapter(Context context, int jumpWeek, int year_c, int month_c, int day_c) {
        super(context);
        dateBeans = SpecialCalendar.getWeekDate(jumpWeek, year_c, month_c, day_c);
        firstDateBean = dateBeans[0];
        firstDatePosition = 0;
    }

    public DateBean getFirstDateBean() {
        return firstDateBean;
    }

    public int getFirstDatePosition() {
        return firstDatePosition;
    }

    @Override
    public void updateItemView(ViewHolder viewHolder, int position) {
        DateBean dateBean = dateBeans[position];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateBean.getDate());
        String tag = dateBean.getTag();
        viewHolder.txDate.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

        /*if (!TextUtils.isEmpty(tag)) {
            //可以根据不同的标签，显示不同颜色
            viewHolder.txDate.setBackgroundResource(R.drawable.calendar_item_event_bg);
        } else {
            viewHolder.txDate.setBackgroundColor(res.getColor(android.R.color.transparent));
        }
        if (dateBean.isHasEvent()) {
            //可以根据不同的标签，显示不同颜色
            if(TextUtils.isEmpty(dateBean.getTag())){
                viewHolder.dot.setBackgroundResource(R.drawable.orange_circle_point2);
            }else {
                //可以根据tag拓展
            }
        }else {
            viewHolder.dot.setBackgroundColor(res.getColor(R.color.transparent));
        }*/

        viewHolder.txDate.setBackgroundColor(res.getColor(android.R.color.transparent));
        viewHolder.dot.setBackgroundColor(res.getColor(R.color.transparent));



        if (dateBean.isIsChoosed()) {
            //选中状态
            viewHolder.txDate.setTextColor(res.getColor(android.R.color.white));
            viewHolder.txDate.setBackgroundResource(R.drawable.calendar_blue_point);
        }else {
            viewHolder.txDate.setTextColor(res.getColor(R.color.bg_white));
            viewHolder.txDate.setBackgroundColor(res.getColor(R.color.transparent));
        }

        if (dateBean.isCurrentDay()) {
            viewHolder.txDate.setText("今");
            //今天
            if(dateBean.isIsChoosed()){
                viewHolder.txDate.setTextColor(res.getColor(android.R.color.white));
            }else {
                viewHolder.txDate.setTextColor(res.getColor(R.color.fsgm_blue));
            }
        }
    }

}
