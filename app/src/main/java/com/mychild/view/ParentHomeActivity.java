package com.mychild.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.customView.CustomDialogClass;
import com.mychild.sharedPreference.PrefManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParentHomeActivity extends BaseActivity implements RequestCompletion,View.OnClickListener{
    public static final String TAG = ParentHomeActivity.class.getSimpleName();
    Spinner sp;
    Button selectStudent;
    String msgToStudentText;
    EditText mesToStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        ImageView homeWork = (ImageView) findViewById(R.id.homework);
        homeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogClass customDialogue=new CustomDialogClass(ParentHomeActivity.this);
                customDialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialogue.show();
            }
        });


  }

    @Override
    public void onRequestCompletion(JSONObject responseJson,JSONArray responseArray) {
        CommonUtils.getLogs("Parent Response");
        Log.i(TAG, responseArray.toString());
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        Constants.showMessage(this,"Sorry",error);

    }

    @Override
    public void onClick(View v) {
        String Url_parent_details = null ;
        PrefManager sharedPref = new PrefManager(this);
        if (CommonUtils.isNetworkAvailable(ParentHomeActivity.this)) {
            Constants.showProgress(ParentHomeActivity.this);
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if(saredpreferences.contains("UserName")){
                Url_parent_details=getString(R.string.base_url)+getString(R.string.parent_url_endpoint)+sharedPref.getUserNameFromSharedPref();
                Log.i("===Url_parent===", Url_parent_details);
            }
            WebServiceCall call = new WebServiceCall(ParentHomeActivity.this);
            call.getCallRequest(Url_parent_details);
        } else {
            CommonUtils.getToastMessage(ParentHomeActivity.this, getString(R.string.no_network_connection));
        }
    }
}
