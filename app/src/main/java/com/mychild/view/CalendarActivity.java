package com.mychild.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.kk.mycalendar.CaldroidFragment;
import com.kk.mycalendar.CaldroidListener;
import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ChildCalendarAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ParentModel;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;


public class CalendarActivity extends BaseFragmentActivity implements RequestCompletion, OnClickListener,IOnSwichChildListener {
    public static final String TAG = CalendarActivity.class.getSimpleName();
	LinearLayout  calendar1;
	ImageView handleImg;
	ListView calListView;
    private TopBar topBar;
    private SwitchChildView switchChild;
    private int selectedChildPosition = 0;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private Dialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
		try {
			setContentView(R.layout.activity_calendar);
            setTopBar();
            switchChildBar();
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
			//
			calendar1 =	(LinearLayout)findViewById(R.id.calendar1);
			//scrollView  = (ScrollView)findViewById(R.id.scrollView);
			CaldroidFragment caldroidFragment = new CaldroidFragment();
			CaldroidFragment.type=0;
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			caldroidFragment.setArguments(args);
			FragmentTransaction t = getSupportFragmentManager().beginTransaction();
			t.replace(R.id.calendar1, caldroidFragment);
			t.commit();
			handleImg = (ImageView)findViewById(R.id.handleImg);
			handleImg.setTag("close");
			((TextView)findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH)+" "+getMonth(cal.get(Calendar.MONTH) + 1).substring(0,3)+" "+cal.get(Calendar.YEAR) );
			handleImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if(v.getTag().toString().equals("open"))
//					{
//						v.setTag("close");
//						calendar1.setVisibility(View.GONE);
//					}
//					else
//					{
						v.setTag("open");
						calendar1.setVisibility(View.VISIBLE);
//					}
				}
			});
			// Setup listener
			final CaldroidListener listener = new CaldroidListener() {
				@Override
				public void onSelectDate(Date date, View view) {
					Constants.showProgress(CalendarActivity.this);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					getChildTimeTabel(cal.get(Calendar.YEAR) +"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH));
				}
				@Override
				public void onChangeMonth(int month, int year) {
					String text = "month: " + month + " year: " + year;
					//	Toast.makeText(getApplicationContext(), text, 	Toast.LENGTH_SHORT).show();
					((TextView)findViewById(R.id.calMonth)).setText(getMonth(month)+" "+year );
				}
				@Override
				public void onLongClickDate(Date date, View view) {
				}
				@Override
				public void onCaldroidViewCreated() {
				}
			};
			caldroidFragment.setCaldroidListener(listener);
			((View)findViewById(R.id.today)).setOnClickListener(new OnClickListener() {

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
					((TextView)findViewById(R.id.calMonth)).setText(getMonth(cal.get(Calendar.MONTH) + 1)+" "+cal.get(Calendar.YEAR) );
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Constants.showProgress(CalendarActivity.this);
		Calendar cal = Calendar.getInstance();
		getChildTimeTabel(cal.get(Calendar.YEAR) +"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH));
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
                break;

            default:
                //Enter code in the event that that no cases match
        }

    }

	@Override
	public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
		CommonUtils.getLogs("timetable Response success");
		Log.i(TAG, responseArray.toString());
		calListView = (ListView) findViewById(R.id.calListView);
		JSONArray ary;
		try {
			ary = new JSONArray(responseArray.toString());
			ChildCalendarAdapter adapter = new ChildCalendarAdapter(CalendarActivity.this, ary);
			calListView.setAdapter(adapter);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Constants.showMessage(this,"Sorry","timetable Response Failure");
		}
		Constants.stopProgress(this);
	}

	@Override
	public void onRequestCompletionError(String error) {
		CommonUtils.getLogs("timetable Response Failure");
		Constants.stopProgress(this);
		Constants.showMessage(this,"Sorry",error);
	}


    public void setSwitchChildDialogueData(){
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void getChildTimeTabel(String date){
        String Url_cal ;
        if (CommonUtils.isNetworkAvailable(this)) {
            Url_cal=getString(R.string.base_url)+getString(R.string.calendar_task)+date;
            Log.i("TimetableURL", Url_cal);
            WebServiceCall call = new WebServiceCall(CalendarActivity.this);
            call.getCallRequest(Url_cal+date);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
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


    @Override
    public void onSwitchChild(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }
}
