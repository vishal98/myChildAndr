package com.mychild.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.CustomDialogueAdapter;
import com.mychild.customView.CustomDialogClass;
import com.mychild.customView.SwitchChildView;
import com.mychild.sharedPreference.PrefManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.webserviceparser.ParentHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParentHomeActivity extends BaseActivity implements RequestCompletion,View.OnClickListener{
    public static final String TAG = ParentHomeActivity.class.getSimpleName();

    PrefManager sharedPref;
    ArrayList<String> childrenList = null;
    ArrayList<HashMap<String, String>> childrenGradeAndSection = null;
    CustomDialogClass customDialogue;
    CustomDialogueAdapter customDialogueAdapter= null;
    private TopBar topBar;
    private SwitchChildView switchChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new PrefManager(this);
        getParentDetailsWebservicescall();
        setContentView(R.layout.activity_parent_home);
        setTopBar();
        switchChildBar();
        setOnClickListener();
  }

    @Override
    public void onRequestCompletion(JSONObject responseJson,JSONArray responseArray) {
        CommonUtils.getLogs("Parent Response success");
        Log.i(TAG, responseArray.toString());
        Constants.stopProgress(this);
        //childrenGradeAndSection = ParentHomeJsonParser.getInstance().getChildrenGradeAndSection(responseArray);
        childrenGradeAndSection = ParentHomeJsonParser.getInstance().getChildrenListwithID(this,responseArray);
        customDialogueAdapter = new CustomDialogueAdapter(this,childrenGradeAndSection);

    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Parent Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this,"Sorry",error);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.switch_child:
                Toast.makeText(this,"Switch Child",Toast.LENGTH_LONG).show();
                customDialogue=new CustomDialogClass(ParentHomeActivity.this,customDialogueAdapter);
                customDialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialogue.setCancelable(true);
                customDialogue.show();

                break;
            case R.id.homework:
                startActivity(new Intent(ParentHomeActivity.this, ChildHomeWorkActivity.class));
                break;
            case R.id.time_table:
                Toast.makeText(this,"Time Table",Toast.LENGTH_LONG).show();
                startActivity(new Intent(ParentHomeActivity.this, ChildrenTimeTableActivity.class));
                break;
            case R.id.exams:
                Toast.makeText(this,"Exams",Toast.LENGTH_LONG).show();
                break;
            case R.id.mail_box:
                Toast.makeText(this,"Mail Box",Toast.LENGTH_LONG).show();
                break;
            case R.id.chat:
                Toast.makeText(this,"Chat",Toast.LENGTH_LONG).show();
                break;
            case R.id.calender:
                Toast.makeText(this,"Calender",Toast.LENGTH_LONG).show();
                break;

            default:
                //Enter code in the event that that no cases match
        }

    }

    public void setTopBar(){
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.my_child));
    }

    public void switchChildBar(){
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText(sharedPref.getUserNameFromSharedPref());
    }



    public void setOnClickListener(){

        ImageView homeWork = (ImageView) findViewById(R.id.homework);
        ImageView timeTable = (ImageView) findViewById(R.id.time_table);
        ImageView exams = (ImageView) findViewById(R.id.exams);
        ImageView mailBox = (ImageView) findViewById(R.id.mail_box);
        ImageView chat = (ImageView) findViewById(R.id.chat);
        ImageView calender = (ImageView) findViewById(R.id.calender);
        homeWork.setOnClickListener(this);
        timeTable.setOnClickListener(this);
        exams.setOnClickListener(this);
        mailBox.setOnClickListener(this);
        chat.setOnClickListener(this);
        calender.setOnClickListener(this);
        switchChild.switchChildBT.setOnClickListener(this);
    }

    public void getParentDetailsWebservicescall(){
        String Url_parent_details = null ;

        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(ParentHomeActivity.this);
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if(saredpreferences.contains("UserName")){
                Url_parent_details=getString(R.string.base_url)+getString(R.string.parent_url_endpoint)+sharedPref.getUserNameFromSharedPref();
                Log.i("===Url_parent===", Url_parent_details);
            }
            WebServiceCall call = new WebServiceCall(ParentHomeActivity.this);
            call.getCallRequest(Url_parent_details);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

}
