/**
 * 文件名 : StickyCalendar.java
 * 包含类名列表 : StickyCalendar
 * 版本信息 : Ver 1.0
 * 创建日期 : 2016年06月19日 22:05
 */
package com.lf.li.fscalendar.view.itcalendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lf.li.fscalendar.R;
import com.lf.li.fscalendar.view.itcalendar.adapter.CalendarAdapter;
import com.lf.li.fscalendar.view.itcalendar.adapter.MonthViewPagerAdapter;
import com.lf.li.fscalendar.view.itcalendar.adapter.WeekViewPagerAdapter;
import com.lf.li.fscalendar.view.itcalendar.utils.DateBean;
import com.lf.li.fscalendar.view.itcalendar.utils.OtherUtils;
import com.lf.li.fscalendar.view.itcalendar.utils.SpecialCalendar;
import com.lf.li.fscalendar.view.itcalendar.view.CalendarView;
import com.lf.li.fscalendar.view.itcalendar.view.MonthView;
import com.lf.li.fscalendar.view.itcalendar.view.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StickyCalendar extends RelativeLayout {

    protected Date mChoosedDate; //用户选中的Date
    protected Date mFakedate; //记录应该显示的Date
    ViewPager mMonthPager;
    ViewPager mWeekPager;
    ViewGroup mCalendarContainer;
    TextView mCollapseTitle;
    ViewGroup mBtnCollapse;
    ViewGroup mListContainer;
    Context context;
    private SpecialCalendar.CalendarType mMode = SpecialCalendar.CalendarType.WEEK;
    /**
     * 日历向左或向右可翻动的天数
     */
    private int INIT_MONTH_PAGER_INDEX = 120; //月视图左右可滑动的页数
    private int INIT_WEEK_PAGER_INDEX = 480; //周视图左右可滑动的页数
    private int CALENDAR_ANIMATION_TIME = 100; //日历动画时间
    private int CALENDAR_LINE_ITEM_HEIGHT = 40; //日历一行dp
    private float offsetDistance;
    private float causeRate;
    private TextView calendarTitle;
    private TextView btnBackToday;
    private ImageView mArrow;
    private LinearLayout calendarLeft, calendarRight;
    private List<View> monthViews = new ArrayList<>();
    private List<View> weekViews = new ArrayList<>();
    private MonthViewPagerAdapter mMonthViewPagerAdapter;
    private WeekViewPagerAdapter mWeekViewPagerAdapter;
    private boolean mIsClick = false;

    public void setCalendarListener(IStickyCalendarListener listener) {
        this.mListener = listener;
    }

    private IStickyCalendarListener mListener;
    /**
     * 点击某个日期回调
     */
    CalendarView.OnCalendarClickListener mCalendarClickListener = new CalendarView
            .OnCalendarClickListener() {
        @Override
        public void onCalendarClick(SpecialCalendar.CalendarType type, int position, DateBean dateBean) {
            if (type == SpecialCalendar.CalendarType.WEEK) {
                onWeekCalendarClick(dateBean);
            } else {
                onMonthCalendarClick(dateBean);
            }
        }
    };

    public StickyCalendar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StickyCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StickyCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(final Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_sticky_calendar, this);
        mMonthPager = (ViewPager) findViewById(R.id.month_pager);
        mBtnCollapse = (ViewGroup) findViewById(R.id.btn_container);
        mCalendarContainer = (ViewGroup) findViewById(R.id.calendar_container);
        mCollapseTitle = (TextView) findViewById(R.id.btn);
        calendarTitle = (TextView) findViewById(R.id.tx_today);
        btnBackToday = (TextView) findViewById(R.id.btn_today);
        mWeekPager = (ViewPager) findViewById(R.id.week_pager);
        mListContainer = (ViewGroup) findViewById(R.id.list_container);
        mArrow = (ImageView) findViewById(R.id.arrow);

        calendarLeft = (LinearLayout) findViewById(R.id.calendar_left);
        calendarRight = (LinearLayout) findViewById(R.id.calendar_right);

        offsetDistance = OtherUtils.dp2px(context, CALENDAR_LINE_ITEM_HEIGHT * (6 - 1));

        btnBackToday.setVisibility(View.GONE);
        btnBackToday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DateBean dateBean = new DateBean();
                dateBean.setDate(Calendar.getInstance().getTime());
                backToToday();
            }
        });
        calendarLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode == SpecialCalendar.CalendarType.WEEK) {
                    mWeekPager.setCurrentItem(mWeekPager.getCurrentItem() - 1, true);
                } else {
                    mMonthPager.setCurrentItem(mMonthPager.getCurrentItem() - 1, true);
                }
            }
        });
        calendarRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode == SpecialCalendar.CalendarType.WEEK) {
                    mWeekPager.setCurrentItem(mWeekPager.getCurrentItem() + 1, true);
                } else {
                    mMonthPager.setCurrentItem(mMonthPager.getCurrentItem() + 1, true);
                }
            }
        });
        mBtnCollapse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

    }


    public void click() {
        //更新文案提示
        if (mMode == SpecialCalendar.CalendarType.MONTH) {
            mCollapseTitle.setText("收起全部");
            mArrow.setRotation(270);
        } else {
            mCollapseTitle.setText("展开全部");
            mArrow.setRotation(90);
        }
        final float currY = mCalendarContainer.getY();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curr = (float) animation.getAnimatedValue();
                if (mMode == SpecialCalendar.CalendarType.MONTH) {
                    mCalendarContainer.setY(currY - offsetDistance * curr);
                    MarginLayoutParams params =
                            (MarginLayoutParams) mListContainer.getLayoutParams();
                    params.setMargins(0, (int) (-offsetDistance * curr), 0, 0);
                    mListContainer.setLayoutParams(params);
                    if (curr >= causeRate) {
                        mWeekPager.setVisibility(View.VISIBLE);
                    }
                } else {
                    mCalendarContainer.setY(currY + offsetDistance * curr);
                    MarginLayoutParams params =
                            (MarginLayoutParams) mListContainer.getLayoutParams();
                    params.setMargins(0, (int) (offsetDistance * (curr - 1)), 0, 0);
                    mListContainer.setLayoutParams(params);
                    if (curr >= 1 - causeRate) {
                        mWeekPager.setVisibility(View.GONE);
                    }
                }

            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mBtnCollapse.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mBtnCollapse.setEnabled(true);
                if (mMode == SpecialCalendar.CalendarType.WEEK) {
                    mMode = SpecialCalendar.CalendarType.MONTH;
                    //展开状态,要判断选中日期是否在当前视图,如果是,那么FakeDate更新为当前选中的日期
                    DateBean[] dateBeans = ((CalendarAdapter) ((CalendarView) monthViews.get(mMonthPager
                            .getCurrentItem() % 3)).getAdapter()).getDateBeans();
                    boolean flag = false;
                    for (DateBean d : dateBeans) {
                        if (SpecialCalendar.isSameDay(mChoosedDate, d.getDate())) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        mFakedate = SpecialCalendar.deepCopyDate(mChoosedDate);
                        mWeekViewPagerAdapter.go2Date(mChoosedDate);
                    }
                } else {
                    mMode = SpecialCalendar.CalendarType.WEEK;
                }
                checkTodayBtn();
            }
        });
        animator.setDuration(CALENDAR_ANIMATION_TIME).start();

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initCalendar();

        if (mMode == SpecialCalendar.CalendarType.WEEK) {
            //如果配置的是周视图,需要收起日历
            float currY = mCalendarContainer.getY();
            mCalendarContainer.setY(currY - offsetDistance);
            MarginLayoutParams params =
                    (MarginLayoutParams) mListContainer.getLayoutParams();
            params.setMargins(0, (int) -offsetDistance, 0, 0);
            mListContainer.setLayoutParams(params);
            mWeekPager.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新红点数据
     *
     * @param eventDates
     */
    public void updateEventDates(final List<Long> eventDates) {
        if (mMonthViewPagerAdapter != null) {
            mMonthViewPagerAdapter.updateEvents(eventDates);
        }

        if (mWeekViewPagerAdapter != null) {
            mWeekViewPagerAdapter.updateEvents(eventDates);
        }
    }

    /**
     * 初始化日历组件,匹配对应的数据
     */
    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        mChoosedDate = Calendar.getInstance().getTime();
        mFakedate = Calendar.getInstance().getTime();
        updateCauseRate(SpecialCalendar.getWeekOfMonthPage(mChoosedDate));
        updateCalendarTitle(calendar.getTime());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 3; i++) {
            MonthView monthView = new MonthView(context, i, year, month);
            monthView.setOnCalendarClickListener(mCalendarClickListener);
            monthViews.add(monthView);

            WeekView weeView = new WeekView(context, i, year, month, day);
            weeView.setOnCalendarClickListener(mCalendarClickListener);
            weekViews.add(weeView);
        }
        mMonthViewPagerAdapter =
                new MonthViewPagerAdapter(context, monthViews, INIT_MONTH_PAGER_INDEX, calendar, mMonthPager);
        mMonthPager.setAdapter(mMonthViewPagerAdapter);
        mMonthPager.setCurrentItem(INIT_MONTH_PAGER_INDEX);
        mMonthPager.setOnPageChangeListener(new OnMonthPagerChangeListener());

        mWeekViewPagerAdapter =
                new WeekViewPagerAdapter(context, weekViews, INIT_WEEK_PAGER_INDEX, calendar, mWeekPager);
        mWeekPager.setAdapter(mWeekViewPagerAdapter);
        mWeekPager.setCurrentItem(INIT_WEEK_PAGER_INDEX);
        mWeekPager.setOnPageChangeListener(new OnWeekChangeListener());
    }

    public void backToToday() {
        DateBean dateBean = new DateBean();
        dateBean.setDate(Calendar.getInstance().getTime());
        if (mMode == SpecialCalendar.CalendarType.WEEK) {
            onWeekCalendarClick(dateBean);
        } else {
            onMonthCalendarClick(dateBean);
        }

        btnBackToday.setVisibility(View.GONE);
    }


    public interface IStickyCalendarListener {
        /**
         * 日期被选中
         *
         * @param date
         */
        void onChooseDate(Date date);

        /**
         * 某一页被选中
         *
         * @param type     1,周 2,月
         * @param position
         */
        void onPageSelected(SpecialCalendar.CalendarType type, int position, Date date);
    }

    class OnMonthPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                onMonthPageSelected(position);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class OnWeekChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                onWeekPageSelected(position);
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 获取Frag容器的id
     *
     * @return
     */
    public int getFragId() {
        return R.id.list_frag;
    }

    /**
     * 控制 "今天" 按钮的显示和隐藏
     */
    private void checkTodayBtn() {
        if (mMode == SpecialCalendar.CalendarType.WEEK) {
            if (SpecialCalendar.isCurrWeek(mFakedate)) {
                btnBackToday.setVisibility(View.GONE);
            } else {
                btnBackToday.setVisibility(View.VISIBLE);
            }
        } else {
            if (SpecialCalendar.isCurrMonth(mFakedate)) {
                btnBackToday.setVisibility(View.GONE);
            } else {
                btnBackToday.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 更新月周视图,周视图显示隐藏的触发点
     *
     * @param position
     */
    private void updateCauseRate(int position) {
        causeRate = position / 6.0f;
    }

    private void updateCalendarTitle(Date date) {
        calendarTitle.setText(OtherUtils.formatDate(date));
        System.out.println("标题:" + OtherUtils.formatDate(date));
    }

    /**
     * 周视图日期被点击
     *
     * @param dateBean
     */
    private void onWeekCalendarClick(DateBean dateBean) {
        mIsClick = true;
        Date tmpDate = SpecialCalendar.deepCopyDate(mFakedate);
        mChoosedDate = SpecialCalendar.deepCopyDate(dateBean.getDate());
        if (SpecialCalendar.needChangeWeek(tmpDate, mChoosedDate)) {
            mWeekViewPagerAdapter.go2Date(mChoosedDate);
            if (mListener != null) {
                mListener.onPageSelected(SpecialCalendar.CalendarType.WEEK, mWeekPager.getCurrentItem(), mChoosedDate);
            }
        } else {
            mWeekViewPagerAdapter.chooseDate(mChoosedDate);
        }

        if (SpecialCalendar.needChangeMonth(tmpDate, mChoosedDate)) {
            mMonthViewPagerAdapter.go2Date(mChoosedDate);
        } else {
            mMonthViewPagerAdapter.chooseDate(mChoosedDate);
        }

        mFakedate = SpecialCalendar.deepCopyDate(mChoosedDate);
        checkTodayBtn();
        updateCalendarTitle(mChoosedDate);
        updateCauseRate(SpecialCalendar.getWeekOfMonthPage(mFakedate));

        if (mListener != null) {
            mListener.onChooseDate(mChoosedDate);
        }
        mIsClick = false;
    }

    /**
     * 月视图日期被点击
     *
     * @param dateBean
     */
    private void onMonthCalendarClick(DateBean dateBean) {
        mIsClick = true;
        Date tmpDate = SpecialCalendar.deepCopyDate(mFakedate);
        mChoosedDate = SpecialCalendar.deepCopyDate(dateBean.getDate());
        if (SpecialCalendar.needChangeMonth(tmpDate, mChoosedDate)) {
            mMonthViewPagerAdapter.go2Date(mChoosedDate);
            if (mListener != null) {
                mListener.onPageSelected(SpecialCalendar.CalendarType.MONTH, mMonthPager.getCurrentItem(), mChoosedDate);
            }
        } else {
            mMonthViewPagerAdapter.chooseDate(mChoosedDate);
        }

        if (SpecialCalendar.needChangeWeek(tmpDate, mChoosedDate)) {
            mWeekViewPagerAdapter.go2Date(mChoosedDate);
        } else {
            mWeekViewPagerAdapter.chooseDate(mChoosedDate);
        }

        mFakedate = SpecialCalendar.deepCopyDate(mChoosedDate);
        checkTodayBtn();
        updateCalendarTitle(mChoosedDate);
        updateCauseRate(SpecialCalendar.getWeekOfMonthPage(mFakedate));

        if (mListener != null) {
            mListener.onChooseDate(mChoosedDate);
        }
        mIsClick = false;
    }

    /**
     * 周视图切页
     *
     * @param position
     */
    private void onWeekPageSelected(int position) {
        if (!mIsClick) {
            Date tmpDate = SpecialCalendar.deepCopyDate(mFakedate);
            Calendar cur = mWeekViewPagerAdapter.getFirstDateBean(position);
            Calendar fakeDate = Calendar.getInstance();
            fakeDate.setTime(mFakedate);
            cur.set(Calendar.DAY_OF_WEEK, fakeDate.get(Calendar.DAY_OF_WEEK));
            mFakedate = cur.getTime();
            updateCalendarTitle(mFakedate);
            checkTodayBtn();
            mWeekViewPagerAdapter.refreshPage(position, mChoosedDate);
            updateCauseRate(cur.get(Calendar.WEEK_OF_MONTH) - 1);
            if (SpecialCalendar.needChangeMonth(tmpDate, mFakedate)) {
                mMonthViewPagerAdapter.go2DatePage(mFakedate);
            }

            if (mMode == SpecialCalendar.CalendarType.WEEK) {
                if (mListener != null && mMode == SpecialCalendar.CalendarType.WEEK) {
                    mListener.onPageSelected(SpecialCalendar.CalendarType.WEEK, position, mFakedate);
                }
            }
        }
    }

    /**
     * 月视图切页
     *
     * @param position
     */
    private void onMonthPageSelected(int position) {
        if (!mIsClick) {
            Date tmpDate = SpecialCalendar.deepCopyDate(mFakedate);
            Calendar cur = mMonthViewPagerAdapter.getFirstDateBean(position);
            Calendar fakeDate = Calendar.getInstance();
            fakeDate.setTime(mFakedate);
            //需要做特殊处理,比如当前月31天,下个月没有31天
            if (fakeDate.get(Calendar.DATE) > cur.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                cur.set(Calendar.DATE, cur.getActualMaximum(Calendar.DAY_OF_MONTH));
            } else {
                cur.set(Calendar.DATE, fakeDate.get(Calendar.DATE));
            }
            mFakedate = cur.getTime();
            updateCalendarTitle(mFakedate);
            checkTodayBtn();
            mMonthViewPagerAdapter.refreshPage(position, mChoosedDate);
            //特殊处理,比如选中了8月11号,Fake是21号,这个时候要将Fake同步为11号
            DateBean[] dateBeans = ((CalendarAdapter) ((CalendarView) monthViews.get(position % 3)).getAdapter()).getDateBeans();
            boolean flag = false;
            for (DateBean d : dateBeans) {
                if (SpecialCalendar.isSameDay(mChoosedDate, d.getDate())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                mFakedate = SpecialCalendar.deepCopyDate(mChoosedDate);
                mWeekViewPagerAdapter.go2Date(mChoosedDate);
                cur.setTime(mChoosedDate);
                updateCauseRate(cur.get(Calendar.WEEK_OF_MONTH) - 1);
            } else {
                updateCauseRate(cur.get(Calendar.WEEK_OF_MONTH) - 1);
                if (SpecialCalendar.needChangeWeek(tmpDate, mFakedate)) {
                    mWeekViewPagerAdapter.go2DatePage(mFakedate);
                }
            }

            if (mMode == SpecialCalendar.CalendarType.MONTH) {
                if (mListener != null && mMode == SpecialCalendar.CalendarType.MONTH) {
                    mListener.onPageSelected(SpecialCalendar.CalendarType.MONTH, position, mFakedate);
                }
            }
        }
    }

    public Date getCurShowDate() {
        return mFakedate;
    }
}
