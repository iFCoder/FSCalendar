package com.lf.li.fscalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lf.li.fscalendar.R;
import com.lf.li.fscalendar.adapter.TestAdapter;
import com.lf.li.fscalendar.fragment.FragmentTodayFinish;
import com.lf.li.fscalendar.fragment.FragmentTodayPlan;
import com.lf.li.fscalendar.view.xiaomicalendar.CaledarAdapter;
import com.lf.li.fscalendar.view.xiaomicalendar.CalendarBean;
import com.lf.li.fscalendar.view.xiaomicalendar.CalendarDateView;
import com.lf.li.fscalendar.view.xiaomicalendar.CalendarLayout;
import com.lf.li.fscalendar.view.xiaomicalendar.CalendarUtil;
import com.lf.li.fscalendar.view.xiaomicalendar.CalendarView;

import java.util.Date;

import static com.lf.li.fscalendar.view.xiaomicalendar.CUtils.px;

/**
 * Created by LiFei on 2017/5/5.
 */

public class XiaoMiActivity extends AppCompatActivity {
    private FragmentTodayPlan fragmentTodayPlan;
    private FragmentTodayFinish fragmentTodayFinish;
    TabLayout mTabPlan;
    CalendarDateView mCalendarDateView;
    ViewPager mVpPlan;
    ImageView calendarLeft,calendaRight;
    CalendarLayout mClCalendar;
    TextView selectTime;
    TextView openFold;
    private TestAdapter mTestAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaomi);
        mTabPlan = (TabLayout) findViewById(R.id.tab_plan);
        mVpPlan = (ViewPager) findViewById(R.id.vp_plan);
        calendarLeft = (ImageView) findViewById(R.id.calendar_left);
        calendaRight = (ImageView) findViewById(R.id.calendar_right);
        selectTime = (TextView) findViewById(R.id.select_time);
        openFold = (TextView) findViewById(R.id.calendar_open_fold);
        mCalendarDateView = (CalendarDateView) findViewById(R.id.calendarDateView);
        mClCalendar = (CalendarLayout) findViewById(R.id.cl_calendar);
        initView();
        setOnClickListener();
    }

    private void initView() {
        int[] data = CalendarUtil.getYMD(new Date());
        selectTime.setText(data[0]+"年"+((data[1] < 10) ? "0" + data[1] : data[1]) + "月");
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_xiaomi, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(48), px(48));
                    convertView.setLayoutParams(params);
                }

                view = (TextView) convertView.findViewById(R.id.text);

                view.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    view.setTextColor(0xffffffff);
                }

                return convertView;
            }
        });

        //点击监听事件
        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                Toast.makeText(XiaoMiActivity.this, bean.year + "-" + getDisPlayNumber(bean.moth) + "-" + getDisPlayNumber(bean.day), Toast.LENGTH_SHORT).show();
            }
        });
        mCalendarDateView.setOnPageListener(new CalendarDateView.onPageListener() {
            @Override
            public void onPage(CalendarBean bean) {
                selectTime.setText(bean.year + "年" + ((bean.moth < 10) ? "0" + bean.moth : bean.moth) + "月");
            }
        });


        if (mVpPlan != null) {
            setupViewPager(mVpPlan);
        }
        mTabPlan.setupWithViewPager(mVpPlan);
        //设置默认选项
        mTabPlan.getTabAt(0).select();
    }

    private void setupViewPager(ViewPager costTabVp) {
        fragmentTodayPlan = new FragmentTodayPlan();
        fragmentTodayFinish = new FragmentTodayFinish();

        mTestAdapter = new TestAdapter(getSupportFragmentManager());

        mTestAdapter.addFragment(fragmentTodayPlan, "今天待办");
        mTestAdapter.addFragment(fragmentTodayFinish, "今天已完成");
        costTabVp.setAdapter(mTestAdapter);
    }

    private void setOnClickListener() {
        mClCalendar.setOnOpenFoldListener(new CalendarLayout.onOpenFoldListener() {
            @Override
            public void change(int type) {
                if (type == mClCalendar.TYPE_OPEN) {
                   openFold.setText("展开日历");
                    mClCalendar.type = mClCalendar.TYPE_FOLD;
                } else {
                    openFold.setText("收起日历");
                    mClCalendar.type = mClCalendar.TYPE_OPEN;
                }
            }
        });
        calendarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarDateView.setCurrentItem(mCalendarDateView.getPositions() - 1, true);
            }
        });
        calendaRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarDateView.setCurrentItem(mCalendarDateView.getPositions() + 1, true);
            }
        });
        openFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClCalendar.getTypes() == mClCalendar.TYPE_OPEN) {
                    openFold.setText("展开日历");
                    mClCalendar.setFOLD();
                } else {
                    openFold.setText("收起日历");
                    mClCalendar.setOPEN();
                }
            }
        });
    }

    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }

}
