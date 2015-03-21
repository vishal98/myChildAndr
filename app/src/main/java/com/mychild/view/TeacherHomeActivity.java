package com.mychild.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.model.StudentDTO;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.webserviceparser.TeacherHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep on 20-03-2015.
 */
public class TeacherHomeActivity extends BaseActivity implements View.OnClickListener, RequestCompletion {
    String teacherName = "test_teacher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        ((Button) findViewById(R.id.message_btn)).setOnClickListener(this);
        ((Button) findViewById(R.id.profile_btn)).setOnClickListener(this);
        ((Button) findViewById(R.id.inbox_btn)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.message_btn:
                if (CommonUtils.isNetworkAvailable(this)) {
                    Constants.showProgress(this);
                    WebServiceCall call = new WebServiceCall(TeacherHomeActivity.this);
                    call.getCallRequest(getString(R.string.url_teacher_student_list));
                    CommonUtils.getLogs("URL is : " + getString(R.string.url_teacher_student_list));

                } else {
                    CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
                }
                break;
            case R.id.profile_btn:
                if (CommonUtils.isNetworkAvailable(this)) {
                    Constants.showProgress(this);
                    WebServiceCall call = new WebServiceCall(TeacherHomeActivity.this);
                    call.getCallRequest(getString(R.string.base_url) + getString(R.string.url_teachers_list) + teacherName);
                    CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teachers_list) + teacherName);
                } else {
                    CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
                }
                break;
            case R.id.inbox_btn:
                break;
            default:
        }

    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Response :" + responseArray);
        Constants.stopProgress(this);
        ArrayList<StudentDTO> studentsList = TeacherHomeJsonParser.getInstance().getStudentsList(responseArray);
    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }
}
