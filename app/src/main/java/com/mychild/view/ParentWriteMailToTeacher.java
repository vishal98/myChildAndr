package com.mychild.view;

import android.os.Bundle;
import android.view.View;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.customView.SwitchChildView;
import com.mychild.utils.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentWriteMailToTeacher extends  BaseActivity implements RequestCompletion, View.OnClickListener {
    private TopBar topBar;
    private SwitchChildView switchChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_writemail_to_teacher);
        setTopBar();
        switchChildBar();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {

    }

    @Override
    public void onRequestCompletionError(String error) {

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
}
