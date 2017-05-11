/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.lf.li.fscalendar.view.itcalendar.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.lf.li.fscalendar.R;
import com.lf.li.fscalendar.view.itcalendar.adapter.WeekViewAdapter;
import com.lf.li.fscalendar.view.itcalendar.utils.DateBean;
import com.lf.li.fscalendar.view.itcalendar.utils.SpecialCalendar;


public class WeekView extends CalendarView {
    private WeekViewAdapter mAdapter;
    private OnCalendarClickListener onCalendarClickListener;

    public WeekView(Context context, int jumpWeek, int year, int month, int day) {
        super(context);
        mAdapter = new WeekViewAdapter(context, jumpWeek, year, month, day);
        initCalendarValues();
        setCalendarValues();
    }

    private void initCalendarValues() {
        setCacheColorHint(getResources().getColor(android.R.color.transparent));
        setHorizontalSpacing(0);
        setVerticalSpacing(0);
        setNumColumns(7);
        setStretchMode(STRETCH_COLUMN_WIDTH);
        setSelector(R.color.transparent);
    }

    private void setCalendarValues() {
        setAdapter(mAdapter);
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateBean dateBean = (DateBean) mAdapter.getItem(position);
                if (onCalendarClickListener != null) {
                    onCalendarClickListener.onCalendarClick(SpecialCalendar.CalendarType.WEEK,position, dateBean);
                }
            }
        });
    }

    public void refreshView(Context context, int jump, int year, int month, int day) {
        mAdapter = new WeekViewAdapter(context, jump, year, month, day);
        setAdapter(mAdapter);
        setCalendarValues();
    }

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        this.onCalendarClickListener = onCalendarClickListener;
    }

    @Override
    public void refreshSelf() {
        mAdapter.notifyDataSetChanged();
    }
}
