package com.lf.li.fscalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lf.li.fscalendar.R;
import com.lf.li.fscalendar.adapter.TestAdapter;
import com.lf.li.fscalendar.fragment.FragmentTodayFinish;
import com.lf.li.fscalendar.fragment.FragmentTodayPlan;
import com.lf.li.fscalendar.view.itcalendar.StickyCalendar;
import com.lf.li.fscalendar.view.itcalendar.utils.SpecialCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LiFei on 2017/5/5.
 */

public class ItActivity extends AppCompatActivity implements StickyCalendar.IStickyCalendarListener {
    private FragmentTodayPlan fragmentTodayPlan;
    private FragmentTodayFinish fragmentTodayFinish;
    private TestAdapter mTestAdapter;
    StickyCalendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_it);
        calendar = (StickyCalendar) findViewById(R.id.it_calendar);
        initView();
        calendar.setCalendarListener(this);
    }

    private void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.plan_tab, null);
        TabLayout mTabPlan = (TabLayout) view.findViewById(R.id.tab_plan);
        ViewPager mVpPlan = (ViewPager) view.findViewById(R.id.vp_plan);
        LinearLayout ll = (LinearLayout) findViewById(calendar.getFragId());
        ll.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

    @Override
    public void onChooseDate(Date date) {
        Toast.makeText(this, new SimpleDateFormat("yyyy-MM-dd").format(date), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageSelected(SpecialCalendar.CalendarType type, int position, Date date) {

    }
}
