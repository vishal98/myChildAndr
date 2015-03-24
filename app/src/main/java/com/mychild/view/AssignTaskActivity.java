package com.mychild.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.adapters.CustomAdapter;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class AssignTaskActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, RequestCompletion {

    private TopBar topBar;
    private Spinner classSpinner;
    private RadioGroup selectStudioRG;
    private Button assignTaskBtn;
    private final int REQUEST_CODE = 1234;
    private boolean updateCheckStatus = false;
    String teacherName = "test_teacher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.assign_task_title));
        classSpinner = (Spinner) findViewById(R.id.class_spinner);
        selectStudioRG = (RadioGroup) findViewById(R.id.select_students_rg);
        assignTaskBtn = (Button) findViewById(R.id.assign_task_btn);
        if (CommonUtils.isNetworkAvailable(this)) {
           /* Constants.showProgress(this);
            WebServiceCall call = new WebServiceCall(AssignTaskActivity.this);
            call.getCallRequest(getString(R.string.base_url) + getString(R.string.url_teachers_list) + teacherName);*/
            CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teachers_list) + teacherName);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
        //register with listeners
        assignTaskBtn.setOnClickListener(this);
        topBar.backArrowIV.setOnClickListener(this);
        selectStudioRG.setOnCheckedChangeListener(this);
        ArrayList<String> studentNameList = new ArrayList<String>();
        studentNameList.add("Sandeep");
        studentNameList.add("Ravi");
        studentNameList.add("Akhil");
        studentNameList.add("Raju");
        studentNameList.add("Shiva");
        studentNameList.add("Sai");
        studentNameList.add("Santhosh");
        studentNameList.add("Sachin");
        studentNameList.add("Dhoni");
        studentNameList.add("Dravid");
        studentNameList.add("Laxman");
        studentNameList.add("Anil");
        studentNameList.add("Ganguly");
        studentNameList.add("Lara");
        CustomAdapter adapter = new CustomAdapter(this, R.layout.drop_down_item, studentNameList);
        classSpinner.setAdapter(adapter);
        adapter = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            case R.id.assign_task_btn:
                break;
            default:
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case -1:
                updateCheckStatus = false;
                break;
            case R.id.select_all_rb:
                break;
            case R.id.select_few_rb:
                if (!updateCheckStatus)
                    startActivityForResult(new Intent(this, SelectStudentActivity.class), REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {
            updateCheckStatus = false;
            CommonUtils.getLogs("User Selected something");
            CommonUtils.getToastMessage(this, "User Selected");
        } else {
            CommonUtils.getLogs("User Nothing Selected");
            CommonUtils.getToastMessage(this, "Nothing Selected");
            updateCheckStatus = true;
            selectStudioRG.clearCheck();

        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Response::::" + responseJson);
        CommonUtils.getLogs("Response::::" + responseArray);
    }

    @Override
    public void onRequestCompletionError(String error) {

    }
}
