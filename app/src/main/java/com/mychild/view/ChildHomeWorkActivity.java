package com.mychild.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ChildHomeworkAdapter;
import com.mychild.customView.CustomDialogClass;
import com.mychild.customView.SwitchChildView;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.webserviceparser.ChildHomeWorkJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/27/15.
 */
public class ChildHomeWorkActivity extends BaseActivity implements RequestCompletion, View.OnClickListener{
    public static final String TAG = ChildHomeWorkActivity.class.getSimpleName();

    ListView homeWorkList;
    CalendarView calender;
    ArrayList<HashMap<String,String>> childrenGradeAndSection;
    private TopBar topBar;
    private SwitchChildView switchChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.showProgress(this);
        //calender = (CalendarView) findViewById(R.id.homework_calender);
        getChildHomworkWebservicescall();
        setContentView(R.layout.activity_child_homework);
        setTopBar();
        switchChildBar();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Homework Response success");
        Log.i(TAG, responseJson.toString());
        homeWorkList = (ListView) findViewById(R.id.homework);
        childrenGradeAndSection = ChildHomeWorkJsonParser.getInstance().getChildrenHomework(responseJson);
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
                CustomDialogClass dialogue = new CustomDialogClass(this);
                dialogue.show();
                break;

            default:
                //Enter code in the event that that no cases match
        }
    }

    public void setTopBar(){
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.home_work));

    }

    public void switchChildBar(){
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
    }


    public void getChildHomworkWebservicescall(){
        String Url_home_work = null ;
        if (CommonUtils.isNetworkAvailable(this)) {
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if(saredpreferences.contains("UserName")){
                Url_home_work ="http://default-environment-8tpprium54.elasticbeanstalk.com/app/getHomework/student/1/30-03-2015";

                Log.i("===Url_Homework===", Url_home_work);
            }
            WebServiceCall call = new WebServiceCall(ChildHomeWorkActivity.this);
            call.getJsonObjectResponse(Url_home_work);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }


}
