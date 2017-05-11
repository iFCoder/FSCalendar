package com.lf.li.fscalendar.view.xiaomicalendar;

/**
 *Created by LiFei on 2017/2/7.
 */

public interface CalendarTopView {

    int[] getCurrentSelectPositon();

    int getItemHeight();

    void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener);

}
