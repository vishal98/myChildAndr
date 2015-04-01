package com.mychild.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ParentInboxAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ParentModel;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;
import com.mychild.webserviceparser.ParentMailBoxParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/29/15.
 */
public class ChildInboxActivity extends BaseActivity implements RequestCompletion, View.OnClickListener, IOnSwichChildListener {
    public static final String TAG = ChildInboxActivity.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;
    ImageView writeMail;
    private ParentModel parentModel = null;
    private Dialog dialog = null;
    private int selectedChildPosition = 0;
    private AppController appController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.showProgress(this);
        setContentView(R.layout.activity_child_inbox);
        setOnClickListener();
        setTopBar();
        switchChildBar();
        inboxWebServiceCall();
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
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
            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
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
        CommonUtils.getLogs("INbox Response success");
        Log.i(TAG, responseJson.toString());
        ArrayList<HashMap<String, String>> mailBox = ParentMailBoxParser.getInstance().getParentMailBox(responseJson);
        ParentInboxAdapter adapter = new ParentInboxAdapter(this, mailBox);
        ListView listView = (ListView) findViewById(R.id.child_time_table_list);
        listView.setAdapter(adapter);
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Inbox Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

    public void setOnClickListener() {
        writeMail = (ImageView) findViewById(R.id.write_mailIV);
        writeMail.setOnClickListener(this);
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.inbox));
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
    }


    public void inboxWebServiceCall() {
        String Url_inbox = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Url_inbox = getString(R.string.base_url) + getString(R.string.parent_chat);
            Log.i("TimetableURL", Url_inbox);
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(Url_inbox);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }
}