package com.mychild.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.customView.SwitchChildView;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentChatAvtivity extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentChatAvtivity.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_chat);
        setTopBar();
        switchChildBar();
    }


    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Timetable Response success");
        Log.i(TAG, responseArray.toString());
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("timetable Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this,"Sorry",error);
    }

    public void setTopBar(){
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.chat));
    }

    public void switchChildBar(){
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
    }
}
