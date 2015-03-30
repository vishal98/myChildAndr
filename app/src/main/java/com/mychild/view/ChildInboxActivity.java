package com.mychild.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.customView.SwitchChildView;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vijay on 3/29/15.
 */
public class ChildInboxActivity extends  BaseActivity implements RequestCompletion, View.OnClickListener {

    private TopBar topBar;
    private SwitchChildView switchChild;
    ImageView writeMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_inbox);
        setOnClickListener();
        setTopBar();
        switchChildBar();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.write_mailIV:
                startActivity(new Intent(ChildInboxActivity.this, ParentWriteMailToTeacher.class));
                break;

            default:
                //Enter code in the event that that no cases match
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {

    }

    @Override
    public void onRequestCompletionError(String error) {

    }

    public void setOnClickListener(){
        writeMail = (ImageView) findViewById(R.id.write_mailIV);
        writeMail.setOnClickListener(this);
    }

    public void setTopBar(){
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.inbox));
    }

    public void switchChildBar(){
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
    }


    public void inboxWebServiceCall(){
        String Url_Chat = null ;
        if (CommonUtils.isNetworkAvailable(this)) {
            Url_Chat=getString(R.string.base_url)+getString(R.string.timetable_child)+"/5/a/wednesday";
            Log.i("TimetableURL", Url_Chat);
            WebServiceCall call = new WebServiceCall(this);
            call.getCallRequest("http://default-environment-8tpprium54.elasticbeanstalk.com/app/timetable/5/a/wednesday");
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
}
