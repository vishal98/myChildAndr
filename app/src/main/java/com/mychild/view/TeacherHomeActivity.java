package com.mychild.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.model.StudentDTO;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
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
        TopBar topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.INVISIBLE);
        topBar.titleTV.setText(getString(R.string.app_name));
        ((ImageView) findViewById(R.id.assign_task_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.time_table_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.exams_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.mail_box_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.chat_iv)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.calender_iv)).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.assign_task_iv:
                startActivity(new Intent(this, AssignTaskActivity.class));
                break;
            case R.id.time_table_iv:
                CommonUtils.getToastMessage(this, "Under Developent");
                break;
            case R.id.exams_iv:
                Intent intent = new Intent(this, ExamsActivity.class);
                intent.putExtra(getString(R.string.key_from), getString(R.string.key_from_teacher));
                startActivity(intent);
                intent = null;
                break;
            case R.id.mail_box_iv:
                CommonUtils.getToastMessage(this, "Under Developent");
                break;
            case R.id.chat_iv:
                CommonUtils.getToastMessage(this, "Under Developent");
                break;
            case R.id.calender_iv:
                CommonUtils.getToastMessage(this, "Under Developent");
                break;

           /* case R.id.message_btn:
                if (CommonUtils.isNetworkAvailable(this)) {
                    Constants.showProgress(this);
                    WebServiceCall call = new WebServiceCall(TeacherHomeActivity.this);
                    call.getCallRequest(getString(R.string.url_teacher_deatils));
                    CommonUtils.getLogs("URL is : " + getString(R.string.url_teacher_deatils));

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
                break;*/
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
