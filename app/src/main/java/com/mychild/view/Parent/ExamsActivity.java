package com.mychild.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ExamsListviewAdapter;
import com.mychild.adapters.ExamsTypesListviewAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnExamChangedListener;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ExamModel;
import com.mychild.model.ParentModel;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.view.CommonToApp.BaseActivity;
import com.mychild.view.CommonToApp.LoginActivity;
import com.mychild.view.R;
import com.mychild.volley.AppController;
import com.mychild.webserviceparser.ExamsJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExamsActivity extends BaseActivity implements View.OnClickListener, RequestCompletion, IOnExamChangedListener, IOnSwichChildListener {

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
    private TopBar topBar;
    String childName;
    int getChildId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        setTopBar();
        switchChildBar();
        setOnClickListener();

        Bundle buddle = getIntent().getExtras();
        if (buddle != null) {
            String fromKey = buddle.getString(getString(R.string.key_from));
            if (fromKey.equals(getString(R.string.key_from_parent))) {
                switchChild.switchChildBT.setVisibility(View.VISIBLE);
                appController = (AppController) getApplicationContext();
                parentModel = appController.getParentsData();
                switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
                selectedChildPosition = appController.getSelectedChild();
            } else if (fromKey.equals(getString(R.string.key_from_teacher))) {
                switchChild.switchChildBT.setVisibility(View.GONE);
            }
        }
        callExamsWebservice(Constants.SET_SWITCH_CHILD_ID);
    }



    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
        if(appController.getParentsData() != null){
            onChangingChild();
        }
        else{
            switchChild.childNameTV.setText("No Child Selected");
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("Response::111" + responseJson);
        examsList = ExamsJsonParser.getInstance().getExamsList(responseJson);
        if (examsList != null && examsList.size() > 0) {
            selectedExamposition = 0;
            setExamScheduleListAdapter(examsList.get(selectedExamposition));
        }
        Constants.stopProgress(this);

    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Error is exams response::" + error);
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

//    @Override
//    public void setAsyncTaskCompletionListener(String object) {
//        CommonUtils.getLogs("Response::::Exams" + object);
//        if (object != null && !object.equals("")) {
//            examsList = ExamsJsonParser.getInstance().getExamsList(object);
//            if (examsList != null && examsList.size() > 0) {
//                selectedExamposition = 0;
//                setExamScheduleListAdapter(examsList.get(selectedExamposition));
//            }
//        }
//    }


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

            case R.id.child_name:
                startActivity(new Intent(this, ProfileFragmentActivity.class));
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
            case R.id.logoutIV:
                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
                Constants.logOut(this);
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                break;

            default:
        }
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
        childName = Constants.getChildNameAfterSelecting(selectedChildPosition,appController.getParentsData());
        getChildId = Constants.getChildIdAfterSelecting(selectedChildPosition,appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        Log.i("Switching child::", Constants.SWITCH_CHILD_FLAG);
        Constants.SET_SWITCH_CHILD_ID = getChildId;
        callExamsWebservice(getChildId);
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }

    private void callExamsWebservice(int childID) {
        String exmas_url = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            exmas_url = getString(R.string.base_url) + getString(R.string.url_child_exam)+childID;
            CommonUtils.getLogs("URL::" + exmas_url);
            WebServiceCall call = new WebServiceCall(ExamsActivity.this);
            call.getJsonObjectResponse(exmas_url);
//            httpConnectThread = new HttpConnectThread(this, null, this);
//            httpConnectThread.execute(exmas_url);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    private void setExamScheduleListAdapter(ExamModel examModel) {
        examTypeTV.setText(examModel.getExamType());
        dateTV.setText("Mar 29 - April 05 2015");
        ExamsListviewAdapter examsListviewAdapter = new ExamsListviewAdapter(this, R.layout.exams_schedule_list_item, examModel.getExamScheduleList());
        examsListView.setAdapter(examsListviewAdapter);
    }

    public void onChangingChild(){
        if(Constants.SWITCH_CHILD_FLAG == null){
            childName = Constants.getChildNameAfterSelecting(0,appController.getParentsData());
            switchChild.childNameTV.setText(childName);
            Constants.SWITCH_CHILD_FLAG = childName;
            Log.i("Setting Default child::",Constants.SWITCH_CHILD_FLAG);
            getChildId = Constants.getChildIdAfterSelecting(0,appController.getParentsData());
            Constants.SET_SWITCH_CHILD_ID = getChildId;
        }
        else {
            switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
        }
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.exams_title));
        topBar.backArrowIV.setOnClickListener(this);
        topBar.logoutIV.setOnClickListener(this);
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
//        switchChild.childNameTV.setText(StorageManager.readString(this, getString(R.string.pref_username), ""));

    }

    public void setOnClickListener() {

        examsListView = (ListView) findViewById(R.id.exams_listview);
        examsIV = (ImageView) findViewById(R.id.exams_iv);
        examTypeTV = (TextView) findViewById(R.id.exam_type_tv);
        dateTV = (TextView) findViewById(R.id.date_tv);
        examsIV.setOnClickListener(this);
        switchChild.switchChildBT.setOnClickListener(this);
        switchChild.childNameTV.setOnClickListener(this);
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


}
