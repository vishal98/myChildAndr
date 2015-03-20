package com.mychild.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.dto.StudentDTO;
import com.mychild.jsonparser.TeacherHomeJsonParser;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep on 20-03-2015.
 */
public class TeacherHomeActivity extends BaseActivity implements View.OnClickListener, RequestCompletion {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        ((Button) findViewById(R.id.button)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            WebServiceCall call = new WebServiceCall(TeacherHomeActivity.this);
            call.getCallRequest(getString(R.string.url_teacher_student_list));
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        ArrayList<StudentDTO> studentsList = TeacherHomeJsonParser.getInstance().getStudentsList(responseArray);
    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }
}
