# FSCalendar
Calendar日历控件（;CalendarDateView;StickyCalendar;渐变效果；开始时间和结束时间，如订酒店选时等）

日历一： 
这个日历是用ListView写的，是上下滑动，按需求要求还需要有选择开始时间和结束时间，所以在GitHub上找并在此基础上修改，原Github地址：https://github.com/NLMartian/SilkCal。 先看看效果： 

![含有开始和结束时间](http://img.blog.csdn.net/20170511170423608?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGYwODE0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
渐变效果 
![这里写图片描述](http://img.blog.csdn.net/20170511171311913?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGYwODE0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

代码修改：
```
<com.lf.li.fscalendar.view.lvcalendar.DayPickerView
        android:id="@+id/calendar_view"
        xmlns:calendar="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white"
        calendar:drawRoundRect="true"   //选中显示圆角还是方角
        calendar:isFold="true"  //ListView是否展开（如展开，高度为match_parent；如不展开，在使用时给其控件设置高度，标准：250dp，其他也行，适配即可）
        calendar:isStartEnd="true"/> //是否为选择开始时间和结束时间
```
这里附上使用方式

```
public class LvActivity extends AppCompatActivity implements DatePickerController {
    private DayPickerView calendarView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);

        calendarView = (DayPickerView) findViewById(R.id.calendar_view);
        calendarView.setController(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_today:
                calendarView.scrollToToday();
                break;
        }
        return true;
    }

    @Override
    public int getMaxYear() {
        return 0;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        Log.e("data", year + "--" + month + "--" + day);
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
        Log.e("data__", selectedDays.getFirst() + "@@@" + selectedDays.getLast());  //月份记得+1
    }
}
```
日历二：
关于日历控件，在网上找了许多，但是都没有能够达到以下需求的日历控件

![这里写图片描述](http://img.blog.csdn.net/20170508100053063?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGYwODE0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
而且自身能力不足就只能从GitHub上面查找，总共找了2个都是在此基础上修改达到上图的需求的，以下就是说明：
1.这个日历控件能够达到的效果是可以通过手势滑动让日历隐藏和现实，而且日历下面还可以添加recycleview等来滑动，其缺点是不能够实现周视图和月视图的转换，原GitHub地址为：https://github.com/codbking/CalendarExaple，我是在第二个基础上修改的。其效果如下：

![这里写图片描述](http://img.blog.csdn.net/20170508101219761?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGYwODE0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
2.这个日历控件是能够实现周视图和月视图的切换，缺点是当日历下面添加Recycleview或者其他滑动控件的时候不能进行手势滑动，原GitHub地址为：https://github.com/keepking/KeepCalendar。其效果图如下图所示：

![这里写图片描述](http://img.blog.csdn.net/20170508101844624?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGYwODE0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
在使用这个日历控件时需要注意的是在初始化日历的时候，图中日历下面的Tab选项卡是没有和日历控件写在同一个布局中的，是通过代码来实现的，即

![这里写图片描述](http://img.blog.csdn.net/20170508102258470?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGYwODE0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

在修改的过程中本人能力有限，大家可以在此基础上再修改
