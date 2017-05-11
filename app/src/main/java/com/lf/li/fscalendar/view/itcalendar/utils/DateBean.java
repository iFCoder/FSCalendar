/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.lf.li.fscalendar.view.itcalendar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBean {

    //针对某一月视图页的数据
    public final static int LAST_MONTH = -1; //上个月
    public final static int CURRENT_MONTH = 0; //当前月
    public final static int NEXT_MONTH = 1; //下个月

    public final static String TAG = "Date_Bean_Tag";

    private Date mDate; //日期


    private boolean mHasEvent = false; //是否有事件
    private boolean mIsChoosed = false; //是否被选中
    private String mTag;//可用于拓展不同事件的展示形式，暂时未使用

    //位置相关,一旦日期确定，以下属性也就确定了
    private boolean mIsCurrentDay = false; //是否是今天，依赖网络时间，本地时间可能不准
    private int mPositionOfMonthPage; //在月视图的位置
    private int mPositionOfWeekPage; //在周视图的位置
    private int mWeekOfMonthPage; //当前月的第几周
    private int mMonthType = 0;

    public DateBean(){

    }

    public DateBean(DateBean db){
        this.mDate = new Date(db.getDate().getTime());
        this.mHasEvent = db.isHasEvent();
        this.mIsChoosed = db.isIsChoosed();
        this.mTag = db.getTag();
        this.mIsCurrentDay = db.isCurrentDay();
        this.mPositionOfMonthPage = db.getPositionOfMonthPage();
        this.mPositionOfWeekPage = db.getPositionOfWeekPage();
        this.mWeekOfMonthPage = db.getWeekOfMonthPage();
        this.mMonthType = db.getMonthType();
    }


    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public boolean isCurrentDay() {
        return mIsCurrentDay;
    }

    public void setIsCurrentDay(boolean isCurrentDay) {
        this.mIsCurrentDay = isCurrentDay;
    }

    public int getMonthType() {
        return mMonthType;
    }

    public void setMonthType(int monthType) {
        this.mMonthType = monthType;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public boolean isHasEvent() {
        return mHasEvent;
    }

    public void setHasEvent(boolean mHasEvent) {
        this.mHasEvent = mHasEvent;
    }

    public boolean isIsChoosed() {
        return mIsChoosed;
    }

    public void setIsChoosed(boolean mIsChoosed) {
        this.mIsChoosed = mIsChoosed;
    }

    public int getPositionOfMonthPage() {
        return mPositionOfMonthPage;
    }

    public void setPositionOfMonthPage(int mPositionOfMonthPage) {
        this.mPositionOfMonthPage = mPositionOfMonthPage;
    }

    public int getPositionOfWeekPage() {
        return mPositionOfWeekPage;
    }

    public void setPositionOfWeekPage(int mPositionOfWeekPage) {
        this.mPositionOfWeekPage = mPositionOfWeekPage;
    }

    public int getWeekOfMonthPage() {
        return mWeekOfMonthPage;
    }

    public void setWeekOfMonthPage(int mWeekOfMonthPage) {
        this.mWeekOfMonthPage = mWeekOfMonthPage;
    }

    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(getDate());
    }

}
