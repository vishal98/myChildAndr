package com.mychild.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.customView.SwitchChildView;
import com.mychild.model.ParentModel;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentWriteMailToTeacher extends  BaseActivity implements RequestCompletion, View.OnClickListener {
    private TopBar topBar;
    private SwitchChildView switchChild;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    private Dialog dialog = null;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_parent_writemail_to_teacher);
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(this);
        setTopBar();
        switchChildBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
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
    public void onBackPressed() {
        super.onBackPressed();
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
        topBar.backArrowIV.setVisibility(View.INVISIBLE);
        topBar.titleTV.setText(getString(R.string.inbox));
    }

    public void switchChildBar(){
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
    }



    public void setSwitchChildDialogueData(){

        appController = (AppController) getApplicationContext();

        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void postEailToServer(){

        if (CommonUtils.isNetworkAvailable(this)) {

        }
        else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }

    }


}
