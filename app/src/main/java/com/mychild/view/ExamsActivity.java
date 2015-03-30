package com.mychild.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.adapters.ExamsListviewAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.model.ExamModel;
import com.mychild.threads.HttpConnectThread;
import com.mychild.utils.AsyncTaskInterface;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.CustomDialog;
import com.mychild.utils.TopBar;
import com.mychild.webserviceparser.ExamsJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExamsActivity extends BaseActivity implements View.OnClickListener, RequestCompletion, AsyncTaskInterface {

    //private String url  = "http://Default-Environment-8tpprium54.elasticbeanstalk.com/Parent/username/";
    private String url = "http://Default-Environment-8tpprium54.elasticbeanstalk.com/Parent/exam/1";
    private SwitchChildView switchChild;
    private ListView examsListView;
    private int selectedExam = 0, selectedExamId = 0;
    private ImageView examsIV;
    private ArrayList<ExamModel> examsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        TopBar topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.exams_title));
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
        examsListView = (ListView) findViewById(R.id.exams_listview);
        examsIV = (ImageView) findViewById(R.id.exams_iv);
        examsIV.setOnClickListener(this);
        callExamsWebservice();

    }

    private void callExamsWebservice() {
        String Url_home_work = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            //  Constants.showProgress(ExamsActivity.this);
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if (saredpreferences.contains("UserName")) {
                Url_home_work = url;
                CommonUtils.getLogs("URL::" + Url_home_work);
            }
            /*WebServiceCall call = new WebServiceCall(ExamsActivity.this);
            call.getCallRequest(Url_home_work);*/
            httpConnectThread = new HttpConnectThread(this, null, this);
            httpConnectThread.execute(Url_home_work);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("Response::111" + responseJson);
        CommonUtils.getLogs("Response::2222" + responseArray);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Error is exams response::" + error);
    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        examsList = ExamsJsonParser.getInstance().getExamsList(object);
        selectedExamId = Integer.parseInt(examsList.get(0).getExamId());
        CommonUtils.getLogs("Response::::" + object);
        setExamScheduleListAdapter(examsList);

    }

    private void setExamScheduleListAdapter(ArrayList<ExamModel> examsList) {
        ExamsListviewAdapter examsListviewAdapter = new ExamsListviewAdapter(this, R.layout.exams_schedule_list_item, examsList.get(selectedExam).getExamScheduleList());
        examsListView.setAdapter(examsListviewAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.switch_child:
                break;
            case R.id.exams_iv:
                CustomDialog.getExamsDialog(this, examsList, selectedExamId);
        }
    }
}
