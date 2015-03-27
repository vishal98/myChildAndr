package com.mychild.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListView;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ChildHomeworkAdapter;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.webserviceparser.ChildHomeWorkJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/27/15.
 */
public class HomeWorkActivity extends BaseActivity implements RequestCompletion {
    public static final String TAG = HomeWorkActivity.class.getSimpleName();

    ListView homeWorkList;
    CalendarView calender;
    ArrayList<HashMap<String,String>> childrenGradeAndSection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.showProgress(this);
        //calender = (CalendarView) findViewById(R.id.homework_calender);
        getChildHomworkWebservicescall();
        setContentView(R.layout.activity_child_homework);

    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Homework Response success");
        Log.i(TAG, responseArray.toString());
        homeWorkList = (ListView) findViewById(R.id.homework);
        childrenGradeAndSection = ChildHomeWorkJsonParser.getInstance().getChildrenHomework(responseArray);
        ChildHomeworkAdapter homeworkAdapter = new ChildHomeworkAdapter(this,childrenGradeAndSection);
        homeWorkList.setAdapter(homeworkAdapter);
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("HomeWork Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this,"Sorry",error);
    }

    public void getChildHomworkWebservicescall(){
        String Url_home_work = null ;
        if (CommonUtils.isNetworkAvailable(this)) {
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if(saredpreferences.contains("UserName")){
                Url_home_work = "http://default-environment-8tpprium54.elasticbeanstalk.com/Parent/studentId/1/teacher/2";
               // Url_home_work=getString(R.string.base_url)+getString(R.string.home_work)+"5/"+"a";
                Log.i("===Url_Homework===", Url_home_work);
            }
            WebServiceCall call = new WebServiceCall(HomeWorkActivity.this);
            call.getCallRequest(Url_home_work);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

}
