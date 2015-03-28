package com.mychild.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.customView.SwitchChildView;
import com.mychild.sharedPreference.PrefManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vijay on 3/28/15.
 */
public class ChildrenTimeTableActivity extends BaseActivity implements RequestCompletion, View.OnClickListener{
    public static final String TAG = ChildrenTimeTableActivity.class.getSimpleName();
    PrefManager sharedPref;
    private TopBar topBar;
    private SwitchChildView switchChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.showProgress(ChildrenTimeTableActivity.this);
        getChildTimeTabel();
        setContentView(R.layout.activity_child_time_tabel);
        setTopBar();
        switchChildBar();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("timetable Response success");
        Log.i(TAG, responseArray.toString());
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("timetable Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this,"Sorry",error);

    }

    @Override
    public void onClick(View v) {
            onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setTopBar(){
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.time_tabel));
    }

    public void switchChildBar(){
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
    }

    public void getChildTimeTabel(){
        String Url_TimeTable = null ;

        if (CommonUtils.isNetworkAvailable(this)) {
            Url_TimeTable=getString(R.string.base_url)+getString(R.string.timrtable_child)+"/5/a";
            Log.i("TimetableURL", Url_TimeTable);
            WebServiceCall call = new WebServiceCall(ChildrenTimeTableActivity.this);
            call.getJsonObjectResponse(Url_TimeTable);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }


}
