package com.lf.li.fscalendar.view.xiaomicalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lf.li.fscalendar.R;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.lf.li.fscalendar.view.xiaomicalendar.CalendarFactory.getMonthOfDayList;


/**
 * Created by LiFei on 2017/2/7.
 */

public class CalendarDateView extends ViewPager implements CalendarTopView {

    HashMap<Integer, CalendarView> views = new HashMap<>();
    private CaledarTopViewChangeListener mCaledarLayoutChangeListener;
    private CalendarView.OnItemClickListener onItemClickListener;

    private LinkedList<CalendarView> cache = new LinkedList();

    private int MAXCOUNT = 6;


    private int row = 6;

    private CaledarAdapter mAdapter;
    private int calendarItemHeight = 0;

    /**
     * viewPage滑动监听
     */
    private onPageListener onPageListener;

    public interface onPageListener {
        void onPage(CalendarBean bean);
    }

    public void setOnPageListener(onPageListener onPageListener) {
        this.onPageListener = onPageListener;
    }

    public void setAdapter(CaledarAdapter adapter) {
        mAdapter = adapter;
        initData();
    }

    public void setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateView);
        row = a.getInteger(R.styleable.CalendarDateView_cbd_calendar_row, 5);
        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int calendarHeight = 0;
        if (getAdapter() != null) {
            CalendarView view = (CalendarView) getChildAt(0);
            if (view != null) {
                calendarHeight = view.getMeasuredHeight();
                calendarItemHeight = view.getItemHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
    }

    private void init() {
        final int[] dateArr = CalendarUtil.getYMD(new Date());

        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                CalendarView view;

                if (!cache.isEmpty()) {
                    view = cache.removeFirst();
                } else {
                    view = new CalendarView(container.getContext(), row);
                }

                view.setOnItemClickListener(onItemClickListener);
                view.setAdapter(mAdapter);

                view.setData(getMonthOfDayList(dateArr[0], dateArr[1] + position - Integer.MAX_VALUE / 2), position == Integer.MAX_VALUE / 2);
                container.addView(view);
                views.put(position, view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                cache.addLast((CalendarView) object);
                views.remove(position);
            }
        });

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                /*if (onItemClickListener != null) {
                    CalendarView view = views.get(position);
                    Object[] obs = view.getSelect();
                    onItemClickListener.onItemClick((View) obs[0], (int) obs[1], (CalendarBean) obs[2]);
                }*/

                if (onPageListener != null) {
                    CalendarView view = views.get(position);
                    Object[] obs = view.getSelect();
                    onPageListener.onPage((CalendarBean) obs[2]);
                }

                mCaledarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
            }
        });
    }

    //获取当前的页
    public int getPositions(){
        return getCurrentItem();
    }

    private void initData() {
        setCurrentItem(Integer.MAX_VALUE / 2, false);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public int[] getCurrentSelectPositon() {
        CalendarView view = views.get(getCurrentItem());
        if (view == null) {
            view = (CalendarView) getChildAt(0);
        }
        if (view != null) {
            return view.getSelectPostion();
        }
        return new int[4];
    }

    @Override
    public int getItemHeight() {
        return calendarItemHeight;
    }

    @Override
    public void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener) {
        mCaledarLayoutChangeListener = listener;
    }


}
