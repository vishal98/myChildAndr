package com.mychild.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
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
        setContentView(R.layout.home);
        sp = (Spinner) findViewById(R.id.select_subject);
        selectStudent = (Button) findViewById(R.id.select_student);
        mesToStudent = (EditText)findViewById(R.id.texttosend);
//      String spinnerText = sp.getSelectedItem().toString();
        msgToStudentText = mesToStudent.getText().toString();
        selectStudent.setEnabled(false);
        spinnerData();
        mesToStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                enableDisableView();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableDisableView();
            }
            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(ParentHomeActivity.this,"afterTextChanged",Toast.LENGTH_LONG).show();
                enableDisableView();
            }
        });
        selectStudent.setOnClickListener(ParentHomeActivity.this);
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
                Url_parent_details=getString(R.string.BASE_URL)+getString(R.string.PARENT_URL_ENDPOINT)+sharedPref.getUserNameFromSharedPref();
                Log.i("===Url_parent===", Url_parent_details);
            }
            WebServiceCall call = new WebServiceCall(ParentHomeActivity.this);
            call.getCallRequest(Url_parent_details);
        } else {
            CommonUtils.getToastMessage(ParentHomeActivity.this, getString(R.string.no_network_connection));
        }
    }

    public  void spinnerData(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.subject_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }

    public void enableDisableView(){
        if (!TextUtils.isEmpty(mesToStudent.getText().toString())) {
            // is not empty
            selectStudent.setEnabled(true);
        }
        else {
            // is empty
            selectStudent.setEnabled(false);
        }
    }
}
