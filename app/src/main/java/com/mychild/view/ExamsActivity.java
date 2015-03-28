package com.mychild.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.customView.SwitchChildView;
import com.mychild.threads.HttpConnectThread;
import com.mychild.utils.AsyncTaskInterface;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;


public class ExamsActivity extends BaseActivity implements View.OnClickListener, RequestCompletion, AsyncTaskInterface {

    //private String url  = "http://Default-Environment-8tpprium54.elasticbeanstalk.com/Parent/username/";
    private String url = "http://Default-Environment-8tpprium54.elasticbeanstalk.com/Parent/exam/1";
    private SwitchChildView switchChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        TopBar topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.exams_title));
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
        callExamsWebservice();

    }

    private void callExamsWebservice() {
        String Url_home_work = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            //  Constants.showProgress(ExamsActivity.this);
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if (saredpreferences.contains("UserName")) {
                Url_home_work = url;
                CommonUtils.getLogs("URL::" + Url_home_work);
            }
            /*WebServiceCall call = new WebServiceCall(ExamsActivity.this);
            call.getCallRequest(Url_home_work);*/
            httpConnectThread = new HttpConnectThread(this, null, this);
            httpConnectThread.execute(Url_home_work);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("Response::111" + responseJson);
        CommonUtils.getLogs("Response::2222" + responseArray);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Error is exams response::" + error);
    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        CommonUtils.getLogs("Response::::" + object);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.switch_child:
                break;
        }
    }
}
