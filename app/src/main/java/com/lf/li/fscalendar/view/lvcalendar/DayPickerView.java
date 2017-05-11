package com.lf.li.fscalendar.view.lvcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.lf.li.fscalendar.R;

import java.util.Calendar;
import java.util.HashMap;

public class DayPickerView extends RecyclerView {
    public static final String TAG = DayPickerView.class.getSimpleName();

    protected Context mContext;
    protected SimpleMonthAdapter mAdapter;
    private DatePickerController mController;
    protected int mCurrentScrollState = 0;
    protected long mPreviousScrollPosition;
    protected int mPreviousScrollState = 0;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;
    private boolean isFold; //是否展开（如展开，高度为match_parent；如不展开，在使用时给其控件设置高度，标准：250dp，其他也行，适配即可）

    public DayPickerView(Context context) {
        this(context, null);
    }

    public DayPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            // this.setOverScrollMode(OVER_SCROLL_NEVER);
            isFold = typedArray.getBoolean(R.styleable.DayPickerView_isFold, false);
            init(context);
        }
    }

    public void setController(DatePickerController mController) {
        this.mController = mController;
        setUpAdapter();
        setAdapter(mAdapter);

        scrollToPosition(12 + Calendar.getInstance().get(Calendar.MONTH));

    }

    public void scrollToToday() {
        smoothScrollToPosition(12 + Calendar.getInstance().get(Calendar.MONTH));
        adjustHeight();
    }


    public void init(Context paramContext) {
        onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final SimpleMonthView child = (SimpleMonthView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }

                mPreviousScrollPosition = dy;
                mPreviousScrollState = mCurrentScrollState;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_DRAGGING) {
                    mAdapter.setDragging(true);
                } else if (newState == SCROLL_STATE_IDLE) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setDragging(false);
                        }
                    }, 150);


                    View firstChild = getChildAt(0);
                    float offset = getY() - firstChild.getY();
                    if (offset > firstChild.getMeasuredHeight() / 2) {
                        stopScroll();
                        smoothScrollBy(0, (int) (getChildAt(1).getY() - getY()));
                    } else {
                        stopScroll();
                        smoothScrollBy(0, -(int) offset);
                    }

                    if (!isFold) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int firstItemHeight = getChildAt(0).getMeasuredHeight();
                                if (getMeasuredHeight() != firstItemHeight) {
                                    ResizeAnimation resizeAnimation = new ResizeAnimation(DayPickerView.this, firstItemHeight);
                                    resizeAnimation.setDuration(200);
                                    startAnimation(resizeAnimation);
                                }
                            }
                        }, 200);
                    }

                }
            }
        };

        setLayoutManager(new LinearLayoutManager(paramContext));
        mContext = paramContext;
        setUpListView();

    }


    protected void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new SimpleMonthAdapter(getContext(), mController, typedArray);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void adjustHeight() {
        if (!isFold) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getChildAt(0) != null) {
                        int firstItemHeight = getChildAt(0).getMeasuredHeight();
                        ResizeAnimation resizeAnimation = new ResizeAnimation(DayPickerView.this, firstItemHeight);
                        resizeAnimation.setDuration(50);
                        startAnimation(resizeAnimation);
                    }
                }
            }, 50);
        }

    }

    protected void setUpListView() {
        setVerticalScrollBarEnabled(false);
        setOnScrollListener(onScrollListener);
        setFadingEdgeLength(0);
    }

    public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() {
        return mAdapter.getSelectedDays();
    }

    public void clearSelectedDays() {
        mAdapter.setSelectedDay(null);
    }

    public void setCountMap(HashMap<SimpleMonthAdapter.CalendarDay, Integer> countMap) {
        mAdapter.setCountMap(countMap);
        this.invalidate();
    }

    protected DatePickerController getController() {
        return mController;
    }

    protected TypedArray getTypedArray() {
        return typedArray;
    }
}