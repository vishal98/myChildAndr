package com.mychild.view;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ChildTimeTabelAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ParentModel;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;
import com.mychild.webserviceparser.ChildTimeTabelParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/28/15.
 */
public class ChildrenTimeTableActivity extends BaseActivity implements RequestCompletion, View.OnClickListener,IOnSwichChildListener {
    public static final String TAG = ChildrenTimeTableActivity.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;
    ChildTimeTabelAdapter adapter;
    ListView timeTabelList;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    private Dialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        Constants.showProgress(ChildrenTimeTableActivity.this);
        getChildTimeTabel();
        setContentView(R.layout.activity_child_time_tabel);
        setTopBar();
        switchChildBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Timetable Response success");
        Log.i(TAG, responseArray.toString());
        timeTabelList = (ListView) findViewById(R.id.child_time_table_list);
        ArrayList<HashMap<String,String>> childrenTimeTable = ChildTimeTabelParser.getInstance().getChildrenTimeTabel(responseArray);
        adapter = new ChildTimeTabelAdapter(this, childrenTimeTable);
        timeTabelList.setAdapter(adapter);
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
        int id = v.getId();
        switch (id) {
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
        }
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

    @Override
    public void onSwitchChild(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }



    public void switchChildBar(){
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);


    }
    public void setSwitchChildDialogueData(){

        appController = (AppController) getApplicationContext();

        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }
    public void getChildTimeTabel(){
        String Url_TimeTable = null ;

        if (CommonUtils.isNetworkAvailable(this)) {
            Url_TimeTable=getString(R.string.base_url)+getString(R.string.timetable_child)+"/5/a/wednesday";
            Log.i("TimetableURL", Url_TimeTable);
            WebServiceCall call = new WebServiceCall(ChildrenTimeTableActivity.this);
            call.getCallRequest("http://default-environment-8tpprium54.elasticbeanstalk.com/app/timetable/5/a/wednesday");
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }



}
