package com.mychild.view;

import android.app.Dialog;
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
import android.widget.Toast;

import com.kk.mycalendar.CaldroidFragment;
import com.kk.mycalendar.CaldroidListener;
import com.kk.mycalendar.WeekdayArrayAdapter;
import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ChildHomeworkAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ParentModel;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;
import com.mychild.webserviceparser.ChildHomeWorkJsonParser;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Vijay on 3/27/15.
 */
public class ChildHomeWorkActivity extends BaseFragmentActivity implements RequestCompletion, View.OnClickListener, IOnSwichChildListener {
    public static final String TAG = ChildHomeWorkActivity.class.getSimpleName();

    ListView homeWorkList;
    ArrayList<HashMap<String, String>> childrenGradeAndSection;
    private TopBar topBar;
    private SwitchChildView switchChild;
    private Dialog dialog = null;
    private int selectedChildPosition = 0;
    private ParentModel parentModel = null;
    private AppController appController = null;
    InfiniteViewPager viewPager;
    int currentIndicator=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_child_homework);
        //appController = (AppController) getApplicationContext();
        Constants.showProgress(this);
        setTopBar();
        switchChildBar();
        //getChildHomworkWebservicescall();
        //parentModel = appController.getParentsData();


        viewPager = (InfiniteViewPager) findViewById(R.id.infinite_viewpager);
        viewPager.setAdapter(new MyInfinitePagerAdapter(0));
        viewPager.setOnInfinitePageChangeListener(new InfiniteViewPager.OnInfinitePageChangeListener() {
            @Override
            public void onPageScrolled(final Object indicator, final float positionOffset,final int positionOffsetPixels) {
                Calendar cal = null ;
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, (Integer.parseInt(String.valueOf(indicator))*7));
                //((TextView)findViewById(R.id.todayDate)).setText(getMonth( cal.get(Calendar.MONTH) +1) +" "+cal.get(Calendar.YEAR) );
            }
            @Override
            public void onPageSelected(final Object indicator) {
                Log.d("InfiniteViewPager", "onPageSelected " + indicator.toString());
                currentIndicator =Integer.parseInt(""+indicator.toString());
            }
            @Override
            public void onPageScrollStateChanged(final int state) {
                Log.d("InfiniteViewPager", "state " + String.valueOf(state));
            }
        });

        GridView weekdayGridView = (GridView) findViewById(R.id.weekday_gridview_main);
        weekdayGridView.setVisibility(View.VISIBLE);
        WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(
                this, android.R.layout.simple_list_item_1, getDaysOfWeek());
        weekdayGridView.setAdapter(weekdaysAdapter);
        Calendar cal = Calendar.getInstance();
        String homeWorkDate = "0" + cal.get(Calendar.DAY_OF_MONTH)+"-"+"0"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.YEAR);
        ((TextView)findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH)+" "+getMonth(cal.get(Calendar.MONTH) + 1).substring(0,3)+" "+cal.get(Calendar.YEAR) );

        final CaldroidFragment dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date",cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR) ,1);
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                //Toast.makeText(ChildHomeWorkActivity.this, "Selected date "+date, Toast.LENGTH_LONG).show();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                dialogCaldroidFragment.dismiss();
                getChildHomworkWebservicescall(getDayFull(cal.get(Calendar.DATE)));
            }
            @Override
            public void onChangeMonth(int month, int year) {
            }
            @Override
            public void onLongClickDate(Date date, View view) {
            }
            @Override
            public void onCaldroidViewCreated() {
            }
        };
        dialogCaldroidFragment.setCaldroidListener(listener);

        ((View)findViewById(R.id.handleImg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialogCaldroidFragment.show(getSupportFragmentManager(),"myDialog");
            }
        });


        Log.i("homeWorkDate",homeWorkDate);
        getChildHomworkWebservicescall(homeWorkDate);

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Homework Response success");
        Log.i(TAG, responseJson.toString());
        homeWorkList = (ListView) findViewById(R.id.homework);
        childrenGradeAndSection = ChildHomeWorkJsonParser.getInstance().getChildrenHomework(responseJson);
        ChildHomeworkAdapter homeworkAdapter = new ChildHomeworkAdapter(this, childrenGradeAndSection);
        homeWorkList.setAdapter(homeworkAdapter);
        Constants.stopProgress(this);

    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("HomeWork Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }

//                CustomDialogClass dialogue = new CustomDialogClass(this);
//                dialogue.show();
                break;

            default:
                //Enter code in the event that that no cases match
        }
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.home_work));

    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
    }

    public void setSwitchChildDialogueData(){
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }


    public void getChildHomworkWebservicescall(String day) {
        String Url_home_work = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            //SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            //if (saredpreferences.contains("UserName")) {
            if (!StorageManager.readString(this, "username", "").isEmpty()){
                Url_home_work = getString(R.string.base_url) +"/app/getHomework/student/1/02-04-2015";

                Log.i("===Url_Homework===", Url_home_work);
            }
            WebServiceCall call = new WebServiceCall(ChildHomeWorkActivity.this);
            call.getJsonObjectResponse(Url_home_work);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }


    @Override
    public void onSwitchChild(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();

    }


    class onDateClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TextView tv  = (TextView)v;
            String selectedDate = tv.getTag().toString();

            Toast.makeText(ChildHomeWorkActivity.this, selectedDate, Toast.LENGTH_LONG).show();
            getChildHomworkWebservicescall(selectedDate);
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
            Calendar cal = null ;
            if(indicator==0)
            {
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -3);

            }
            if(indicator<0)
            {
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, (indicator*7)-3);
            }
            if(indicator>0)
            {
                cal = Calendar.getInstance();
                if(indicator==1)
                    cal.add(Calendar.DAY_OF_YEAR, 4);
                else
                    cal.add(Calendar.DAY_OF_YEAR, (indicator*7)-3);
            }
            text1.setText(""+cal.get(Calendar.DATE));
            text1.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text1.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text2.setText(""+cal.get(Calendar.DATE));
            text2.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text2.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text3.setText(""+cal.get(Calendar.DATE));
            text3.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text3.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text4.setText(""+cal.get(Calendar.DATE));
            text4.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text4.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text5.setText(""+cal.get(Calendar.DATE));
            text5.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text5.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text6.setText(""+cal.get(Calendar.DATE));
            text6.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text6.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text7.setText(""+cal.get(Calendar.DATE));
            text7.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text7.setOnClickListener(new onDateClickListner());

            layout.setTag(indicator);
            if(indicator==0)
            {
                text4.setTextColor(Color.parseColor("#FF0000"));
                //				myTypefaceLight = Typeface.createFromAsset(getActivity().getAssets(),
                //						"font/roboto_black.ttf");
                //				text4.setTypeface(myTypefaceLight);
            }	return layout;
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

        public void checkAndChangeSelectedcolor(TextView tv ,Date current )
        {
        }
    }

    protected ArrayList<String> getDaysOfWeek()
    {
        ArrayList<String> list = new ArrayList<String>();
        Calendar cal =   Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -3);
        for (int i = 0; i < 7; i++)
        {
            list.add(getDay(cal.get(Calendar.DAY_OF_WEEK)));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }

    public String getDayFull(int dayOfWeek)
    {
        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek)
        {
            weekDay = "monday";
        } else if (Calendar.TUESDAY == dayOfWeek)
        {
            weekDay = "tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek)
        {
            weekDay = "wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek)
        {
            weekDay = "thursday";
        } else if (Calendar.FRIDAY == dayOfWeek)
        {
            weekDay = "friday";
        } else if (Calendar.SATURDAY == dayOfWeek)
        {
            weekDay = "saturday";
        } else if (Calendar.SUNDAY == dayOfWeek)
        {
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

    public String getMonth(int month)
    {
        switch (month)
        {
            case 1: return "JANUARY";
            case 2: return "FEBRUARY";
            case 3: return "MARCH";
            case 4: return "APRIL";
            case 5: return "MAY";
            case 6: return "JUNE";
            case 7: return "JULY";
            case 8: return "AUGUST";
            case 9: return "SEPTEMBER";
            case 10: return "OCTOBER";
            case 11: return "NOVEMBER";
            case 12: return "DECEMBER";
            default: return "";
        }
    }



}
