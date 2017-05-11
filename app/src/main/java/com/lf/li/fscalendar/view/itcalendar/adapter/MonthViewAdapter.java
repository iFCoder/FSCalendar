/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.lf.li.fscalendar.view.itcalendar.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.lf.li.fscalendar.R;
import com.lf.li.fscalendar.view.itcalendar.utils.DateBean;
import com.lf.li.fscalendar.view.itcalendar.utils.SpecialCalendar;

import java.util.Calendar;

public class MonthViewAdapter extends CalendarAdapter {

    private DateBean firstDateBean;
    private int firstDatePosition;

    public MonthViewAdapter(Context context, int jumpMonth, int year_c, int month_c) {
        super(context);
        dateBeans = SpecialCalendar.getMonthDate(jumpMonth, year_c, month_c);
        for(int i=0;i<7;++i){
            if (DateBean.CURRENT_MONTH == dateBeans[i].getMonthType()) {
                //如果是当前日期
                if (firstDateBean == null) {
                    firstDateBean = dateBeans[i];
                    firstDatePosition = i;
                }
            }
        }
    }

    public DateBean getFirstDateBean() {
        return firstDateBean;
    }

    public int getFirstDatePosition() {
        return firstDatePosition;
    }

    @Override
    public void updateItemView(CalendarAdapter.ViewHolder viewHolder, int position) {
        DateBean dateBean = dateBeans[position];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateBean.getDate());
        String tag = dateBean.getTag();
        viewHolder.txDate.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        if (DateBean.CURRENT_MONTH == dateBean.getMonthType()) {
            viewHolder.txDate.setTextColor(res.getColor(R.color.bg_white));
        } else {
            viewHolder.txDate.setTextColor(res.getColor(R.color.text_color_normal));
        }

        if (dateBean.isHasEvent()) {
            //可以根据不同的标签，显示不同颜色
            if(TextUtils.isEmpty(dateBean.getTag())){
                viewHolder.dot.setBackgroundResource(R.drawable.calendar_blue_point);
            }else {
                //可以根据tag拓展
            }
        }else {
            viewHolder.dot.setBackgroundColor(res.getColor(R.color.transparent));
        }

        if (dateBean.isIsChoosed()) {
            //选中状态
            if (DateBean.CURRENT_MONTH == dateBean.getMonthType()) {
                viewHolder.txDate.setTextColor(res.getColor(android.R.color.white));
                viewHolder.txDate.setBackgroundResource(R.drawable.calendar_blue_point);
            } else {
                viewHolder.txDate.setTextColor(res.getColor(R.color.calendar_text_color_disable));
                viewHolder.txDate.setBackgroundColor(res.getColor(R.color.transparent));
            }
        }else {
            if (DateBean.CURRENT_MONTH == dateBean.getMonthType()) {
                viewHolder.txDate.setTextColor(res.getColor(R.color.bg_white));
            } else {
                viewHolder.txDate.setTextColor(res.getColor(R.color.text_color_normal));
            }
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
