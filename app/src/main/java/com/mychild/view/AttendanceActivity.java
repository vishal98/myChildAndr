package com.mychild.view;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kk.mycalendar.CaldroidFragment;
import com.kk.mycalendar.CaldroidListener;
import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class AttendanceActivity extends BaseFragmentActivity implements RequestCompletion {
    LinearLayout calendar1;
    ImageView handleImg;
    ScrollView scrollView;
    public static final String TAG = ChildrenTimeTableActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_attendance);
            //	Calendar minYear = Calendar.getInstance();
            //			minYear.add(Calendar.YEAR, -1);
            //
            //			Calendar maxYear = Calendar.getInstance();
            //			maxYear.add(Calendar.DAY_OF_WEEK, 1);
            //			CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
            //			Date today = new Date();
            //			calendar.setOnDateSelectedListener(this);
            //			calendar.init( minYear.getTime(),maxYear.getTime())
            //		.withSelectedDate(today);
            calendar1 = (LinearLayout) findViewById(R.id.calendar1);
            //scrollView  = (ScrollView)findViewById(R.id.scrollView);
            CaldroidFragment caldroidFragment = new CaldroidFragment();
            CaldroidFragment.type = 0;
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar1, caldroidFragment);
            t.commit();
            handleImg = (ImageView) findViewById(R.id.handleImg);
            handleImg.setTag("close");
            ((TextView) findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH) + " " + getMonth(cal.get(Calendar.MONTH) + 1).substring(0, 3) + " " + cal.get(Calendar.YEAR));
            handleImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    if (v.getTag().toString().equals("open")) {
//                        v.setTag("close");
//                        calendar1.setVisibility(View.GONE);
//                    } else {
                        v.setTag("open");
                        calendar1.setVisibility(View.VISIBLE);
//                    }
                }
            });
            // Setup listener
            final CaldroidListener listener = new CaldroidListener() {
                @Override
                public void onSelectDate(Date date, View view) {
//					Calendar cal = Calendar.getInstance();
//					cal.setTime(date);
                }
                @Override
                public void onChangeMonth(int month, int year) {
                    String text = "month: " + month + " year: " + year;
                    //	Toast.makeText(getApplicationContext(), text, 	Toast.LENGTH_SHORT).show();
                    ((TextView) findViewById(R.id.calMonth)).setText(getMonth(month) + " " + year);
                    getAttendance("" + month, "" + year);
                }
                @Override
                public void onLongClickDate(Date date, View view) {
                }
                @Override
                public void onCaldroidViewCreated() {
                }
            };
            caldroidFragment.setCaldroidListener(listener);
            ((View) findViewById(R.id.today)).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CaldroidFragment caldroidFragment = new CaldroidFragment();
                    Bundle args = new Bundle();
                    Calendar cal = Calendar.getInstance();
                    args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                    args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                    caldroidFragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    caldroidFragment.setCaldroidListener(listener);
                    t.replace(R.id.calendar1, caldroidFragment);
                    t.commit();
                    ((TextView) findViewById(R.id.calMonth)).setText(getMonth(cal.get(Calendar.MONTH) + 1) + " " + cal.get(Calendar.YEAR));
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        getAttendance("" + (cal.get(Calendar.MONTH) + 1), "" + cal.get(Calendar.DAY_OF_MONTH));
    }

    public void getAttendance(String month, String year) {
        String Url_cal = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Url_cal = getString(R.string.base_url) + getString(R.string.attendance) + month + "/" + year;
            Log.i("Attendance URl", Url_cal);
            WebServiceCall call = new WebServiceCall(AttendanceActivity.this);
            call.getJsonObjectResponse(Url_cal);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {


        Log.i(TAG, responseJson.toString());

        try {
            ((TextView) findViewById(R.id.todalTv)).setText(responseJson.getString("total_working_days"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            ((TextView) findViewById(R.id.presentTv)).setText(responseJson.getString("present_days"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            ((TextView) findViewById(R.id.absentTv)).setText(responseJson.getString("total_absent_days"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("timetable Response Failure");
        Constants.stopProgress(AttendanceActivity.this);
        Constants.showMessage(this, "Sorry", error);

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
