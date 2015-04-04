package com.mychild.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kk.mycalendar.WeekdayArrayAdapter;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.StudentsListAdapter;
import com.mychild.interfaces.AsyncTaskInterface;
import com.mychild.interfaces.IOnCheckedChangeListener;
import com.mychild.model.StudentDTO;
import com.mychild.model.TeacherModel;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.threads.HttpConnectThread;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.TopBar;
import com.mychild.webserviceparser.TeacherHomeJsonParser;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AttendenceActivity extends BaseActivity implements View.OnClickListener, AsyncTaskInterface, IOnCheckedChangeListener {

    private TopBar topBar;
    private String teacherName = "";
    private ListView studentsListview;
    private InfiniteViewPager viewPager;
    int currentIndicator = 0;
    private TextView doneTV;
    private StudentsListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        studentsListview = (ListView) findViewById(R.id.students_listview);
        doneTV = (TextView) findViewById(R.id.done_tv);
        doneTV.setOnClickListener(this);

        viewPager = (InfiniteViewPager) findViewById(R.id.infinite_viewpager);
        viewPager.setAdapter(new MyInfinitePagerAdapter(0));
        viewPager.setOnInfinitePageChangeListener(new InfiniteViewPager.OnInfinitePageChangeListener() {
            @Override
            public void onPageScrolled(final Object indicator, final float positionOffset, final int positionOffsetPixels) {
                Calendar cal = null;
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, (Integer.parseInt(String.valueOf(indicator)) * 7));
                //((TextView)findViewById(R.id.todayDate)).setText(getMonth( cal.get(Calendar.MONTH) +1) +" "+cal.get(Calendar.YEAR) );
            }

            @Override
            public void onPageSelected(final Object indicator) {
                Log.d("InfiniteViewPager", "onPageSelected " + indicator.toString());
                currentIndicator = Integer.parseInt("" + indicator.toString());
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                Log.d("InfiniteViewPager", "state " + String.valueOf(state));
            }
        });

        GridView weekdayGridView = (GridView) findViewById(R.id.weekday_gridview_main);
        weekdayGridView.setVisibility(View.VISIBLE);
        WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(this, android.R.layout.simple_list_item_1, getDaysOfWeek());
        weekdayGridView.setAdapter(weekdaysAdapter);
        Calendar cal = Calendar.getInstance();
        ((TextView) findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH) + " " + getMonth(cal.get(Calendar.MONTH) + 1).substring(0, 3) + " " + cal.get(Calendar.YEAR));


        httpConnectThread = new HttpConnectThread(this, null, this);
        String url = getString(R.string.base_url) + getString(R.string.url_teacher_deatils);
        teacherName = "/" + StorageManager.readString(this, getString(R.string.pref_username), "");
        httpConnectThread.execute(url + teacherName);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            case R.id.done_tv:
                if (doneTV.getText().toString().equals(getString(R.string.done_caps))) {
                    CommonUtils.getLogs("DOne");
                } else if (doneTV.getText().toString().equals(getString(R.string.start_caps))) {
                    adapter.selectAll();
                    CommonUtils.getLogs("start");
                }
                break;

            default:
        }
    }

    @Override
    public void checkedStateChanged(StudentDTO studentDTO, boolean isChecked) {
        if (adapter.mSelectedItemsIds.size() > 0) {
            doneTV.setText(getString(R.string.done_caps));
        } else {
            doneTV.setText(getString(R.string.start_caps));
        }
    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        if (object != null) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(object);
                TeacherModel teacherModel = TeacherHomeJsonParser.getInstance().getTeacherDetails(obj);

                adapter = new StudentsListAdapter(this, R.layout.select_student_list_item, teacherModel.getGradeModels().get(0).getStudentsModels());
                studentsListview.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.network_error));
        }
        CommonUtils.getLogs("Response::::" + object);
    }

    public void getChildTimeTabel(String day) {
        String Url_TimeTable = null;
        //Constants.showProgress(ChildrenTimeTableActivity.this);
        if (CommonUtils.isNetworkAvailable(this)) {
            Url_TimeTable = getString(R.string.base_url) + getString(R.string.timetable_child) + "/5/a/" + day;
            Log.i("TimetableURL", Url_TimeTable);
            WebServiceCall call = new WebServiceCall(this);
            call.getCallRequest(Url_TimeTable);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }


    class onDateClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v;
            String selectedDate = tv.getTag().toString();
//			Toast.makeText(ChildrenTimeTableActivity.this, selectedDate, Toast.LENGTH_LONG).show();
            getChildTimeTabel(selectedDate);
        }
    }

    private class MyInfinitePagerAdapter extends InfinitePagerAdapter<Integer> {
        /**
         * Standard constructor.
         *
         * @param initValue the initial indicator value the ViewPager should start with.
         */

        public MyInfinitePagerAdapter(final Integer initValue) {
            super(initValue);
        }

        @Override
        public ViewGroup instantiateItem(Integer indicator) {
            Log.d("InfiniteViewPager", "instantiating page " + indicator);
            final LinearLayout layout = (LinearLayout) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.date_view_pager, null);
//            final TextView text = (TextView) layout.findViewById(R.id.moving_view_x);
//            text.setText(String.format("Page %s", indicator));
//            Log.i("InfiniteViewPager", String.format("textView.text() == %s", text.getText()));
            TextView text1 = (TextView) layout.findViewById(R.id.text1);
            TextView text2 = (TextView) layout.findViewById(R.id.text2);
            TextView text3 = (TextView) layout.findViewById(R.id.text3);
            TextView text4 = (TextView) layout.findViewById(R.id.text4);
            TextView text5 = (TextView) layout.findViewById(R.id.text5);
            TextView text6 = (TextView) layout.findViewById(R.id.text6);
            TextView text7 = (TextView) layout.findViewById(R.id.text7);
            Calendar cal = null;
            if (indicator == 0) {
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -3);

            }
            if (indicator < 0) {
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, (indicator * 7) - 3);
            }
            if (indicator > 0) {
                cal = Calendar.getInstance();
                if (indicator == 1)
                    cal.add(Calendar.DAY_OF_YEAR, 4);
                else
                    cal.add(Calendar.DAY_OF_YEAR, (indicator * 7) - 3);
            }
            text1.setText("" + cal.get(Calendar.DATE));
            text1.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text1.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text2.setText("" + cal.get(Calendar.DATE));
            text2.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text2.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text3.setText("" + cal.get(Calendar.DATE));
            text3.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text3.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text4.setText("" + cal.get(Calendar.DATE));
            text4.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text4.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text5.setText("" + cal.get(Calendar.DATE));
            text5.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text5.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text6.setText("" + cal.get(Calendar.DATE));
            text6.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text6.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text7.setText("" + cal.get(Calendar.DATE));
            text7.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text7.setOnClickListener(new onDateClickListner());

            layout.setTag(indicator);
            if (indicator == 0) {
                text4.setTextColor(Color.parseColor("#FF0000"));
                //				myTypefaceLight = Typeface.createFromAsset(getActivity().getAssets(),
                //						"font/roboto_black.ttf");
                //				text4.setTypeface(myTypefaceLight);
            }
            return layout;
        }

        @Override
        public Integer getNextIndicator() {
            return getCurrentIndicator() + 1;
        }

        @Override
        public Integer getPreviousIndicator() {
            return getCurrentIndicator() - 1;
        }

        @Override
        public String getStringRepresentation(final Integer currentIndicator) {
            return String.valueOf(currentIndicator);
        }

        @Override
        public Integer convertToIndicator(final String representation) {
            return Integer.valueOf(representation);
        }

        public void checkAndChangeSelectedcolor(TextView tv, Date current) {
        }
    }

    protected ArrayList<String> getDaysOfWeek() {
        ArrayList<String> list = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -3);
        for (int i = 0; i < 7; i++) {
            list.add(getDay(cal.get(Calendar.DAY_OF_WEEK)));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }

    public String getDayFull(int dayOfWeek) {
        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "monday";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "thursday";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "friday";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "saturday";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "sunday";
        }
        return weekDay;

    }

    public static String getDay(int dayOfWeek) {
        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "MON";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "TUE";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "WED";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "THU";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "FRI";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "SATY";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "SUN";
        }
        return weekDay;

    }

    public String getMonth(int month) {
        switch (month) {
            case 1:
                return "JANUARY";
            case 2:
                return "FEBRUARY";
            case 3:
                return "MARCH";
            case 4:
                return "APRIL";
            case 5:
                return "MAY";
            case 6:
                return "JUNE";
            case 7:
                return "JULY";
            case 8:
                return "AUGUST";
            case 9:
                return "SEPTEMBER";
            case 10:
                return "OCTOBER";
            case 11:
                return "NOVEMBER";
            case 12:
                return "DECEMBER";
            default:
                return "";
        }
    }
}
