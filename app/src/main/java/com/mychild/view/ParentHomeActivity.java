package com.mychild.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.CustomDialogueAdapter;
import com.mychild.customView.CustomDialogClass;
import com.mychild.sharedPreference.PrefManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.webserviceparser.ParentHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentHomeActivity extends BaseActivity implements RequestCompletion,View.OnClickListener{
    public static final String TAG = ParentHomeActivity.class.getSimpleName();
    Spinner sp;
    Button selectStudent;
    String msgToStudentText;
    EditText mesToStudent;
    PrefManager sharedPref;
    ArrayList<String> childrenList = null;
    CustomDialogueAdapter customDialogueAdapter= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new PrefManager(this);
        getParentDetails();
        setContentView(R.layout.activity_parent_home);
        ImageView homeWork = (ImageView) findViewById(R.id.homework);
        TextView parentName = (TextView) findViewById(R.id.parent_name);
        Button switchChild = (Button) findViewById(R.id.switch_child);
        parentName.setText(sharedPref.getUserNameFromSharedPref());
        switchChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogClass customDialogue=new CustomDialogClass(ParentHomeActivity.this,customDialogueAdapter);
                customDialogue.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialogue.show();
            }
        });


  }

    @Override
    public void onRequestCompletion(JSONObject responseJson,JSONArray responseArray) {
        CommonUtils.getLogs("Parent Response success");
        Log.i(TAG, responseArray.toString());
        Constants.stopProgress(this);
        childrenList = ParentHomeJsonParser.getInstance().getChildrenList(responseArray);
        customDialogueAdapter = new CustomDialogueAdapter(this,childrenList);

    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Parent Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this,"Sorry",error);

    }

    @Override
    public void onClick(View v) {

    }

    public void getParentDetails(){
        String Url_parent_details = null ;

        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(ParentHomeActivity.this);
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if(saredpreferences.contains("UserName")){
                Url_parent_details=getString(R.string.base_url)+getString(R.string.parent_url_endpoint)+sharedPref.getUserNameFromSharedPref();
                Log.i("===Url_parent===", Url_parent_details);
            }
            WebServiceCall call = new WebServiceCall(ParentHomeActivity.this);
            call.getCallRequest(Url_parent_details);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
}
