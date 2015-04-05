package com.mychild.view;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.adapters.ExamsListviewAdapter;
import com.mychild.adapters.ExamsTypesListviewAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.AsyncTaskInterface;
import com.mychild.interfaces.IOnExamChangedListener;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ExamModel;
import com.mychild.model.ParentModel;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.threads.HttpConnectThread;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;
import com.mychild.webserviceparser.ExamsJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExamsActivity extends BaseActivity implements View.OnClickListener, RequestCompletion, AsyncTaskInterface, IOnExamChangedListener, IOnSwichChildListener {

    private SwitchChildView switchChild;
    private ListView examsListView;
    private int selectedChildPosition = 0, selectedExamposition = 0;
    private ImageView examsIV;
    private TextView examTypeTV, dateTV;
    private ArrayList<ExamModel> examsList;
    private Dialog examsTypeDialog = null;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        TopBar topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.exams_title));
        topBar.backArrowIV.setOnClickListener(this);
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText(StorageManager.readString(this, getString(R.string.pref_username), ""));
        switchChild.switchChildBT.setOnClickListener(this);
        examsListView = (ListView) findViewById(R.id.exams_listview);
        examsIV = (ImageView) findViewById(R.id.exams_iv);
        examTypeTV = (TextView) findViewById(R.id.exam_type_tv);
        dateTV = (TextView) findViewById(R.id.date_tv);
        examsIV.setOnClickListener(this);
        Bundle buddle = getIntent().getExtras();
        if (buddle != null) {
            String fromKey = buddle.getString(getString(R.string.key_from));
            if (fromKey.equals(getString(R.string.key_from_parent))) {
                switchChild.switchChildBT.setVisibility(View.VISIBLE);
                appController = (AppController) getApplicationContext();
                parentModel = appController.getParentsData();
                switchChild.parentNameTV.setText(parentModel.getName());
                selectedChildPosition = appController.getSelectedChild();
            } else if (fromKey.equals(getString(R.string.key_from_teacher))) {
                switchChild.switchChildBT.setVisibility(View.GONE);
            }
        }
        callExamsWebservice();


    }

    private void callExamsWebservice() {
        String Url_home_work = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            //  Constants.showProgress(ExamsActivity.this);
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if (saredpreferences.contains("UserName")) {
                Url_home_work = getString(R.string.base_url) + getString(R.string.url_teacher_exam);
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
        CommonUtils.getLogs("Response::::Exams" + object);
        if (object != null && !object.equals("")) {
            examsList = ExamsJsonParser.getInstance().getExamsList(object);
            if (examsList.size() > 0) {
                selectedExamposition = 0;
                setExamScheduleListAdapter(examsList.get(selectedExamposition));
            }
        }
    }

    private void setExamScheduleListAdapter(ExamModel examModel) {
        examTypeTV.setText(examModel.getExamType());
        dateTV.setText("Mar 29 - April 05 2015");
        ExamsListviewAdapter examsListviewAdapter = new ExamsListviewAdapter(this, R.layout.exams_schedule_list_item, examModel.getExamScheduleList());
        examsListView.setAdapter(examsListviewAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.exams_iv:
                examsTypeDialog = getExamsDialog(examsList, selectedExamposition);
                break;
            case R.id.cancel_btn:
                examsTypeDialog.dismiss();
                break;
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            default:
        }
    }

    private Dialog getExamsDialog(ArrayList<ExamModel> list, int examPosition) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exams);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);
        //cancelBtn.setOnClickListener();
        ListView examsListview = (ListView) dialog.findViewById(R.id.exams_types_lv);
        ExamsTypesListviewAdapter examsTypesListviewAdapter = new ExamsTypesListviewAdapter(this, R.layout.exam_type_listview_item, list, examPosition);
        examsListview.setAdapter(examsTypesListviewAdapter);
        dialog.show();
        return dialog;
    }

    @Override
    public void onExamChanged(int position, boolean isChecked) {
        if (isChecked) {
            examsTypeDialog.dismiss();
            selectedExamposition = position;
            setExamScheduleListAdapter(examsList.get(selectedExamposition));
        }
    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }
}
