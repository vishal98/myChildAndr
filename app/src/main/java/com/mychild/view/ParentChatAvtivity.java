package com.mychild.view;

import android.content.SharedPreferences;
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
import com.mychild.webserviceparser.ParentChatHistoryParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentChatAvtivity extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentChatAvtivity.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;
    PrefManager sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new PrefManager(this);
        getChatHistoryWebserviceCall();
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
        CommonUtils.getLogs("Chat Response success");
        Log.i(TAG, responseJson.toString());
        ArrayList<HashMap<String, String>> chatList = ParentChatHistoryParser.getInstance().getTeacherListForChat(responseJson);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Chat Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.chat));
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
    }

    public void getChatHistoryWebserviceCall() {
        String Url_ChatHistory = null;

        if (CommonUtils.isNetworkAvailable(this)) {
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if (saredpreferences.contains("UserName")) {
                Url_ChatHistory = getString(R.string.base_url) + getString(R.string.parent_chat) + sharedPref.getUserNameFromSharedPref();
                Log.i("Url_ChatHistory::", Url_ChatHistory);
            }
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(Url_ChatHistory);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
}
