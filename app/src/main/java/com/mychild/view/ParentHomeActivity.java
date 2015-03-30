package com.mychild.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.CustomDialogueAdapter;
import com.mychild.adapters.ExamsTypesListviewAdapter;
import com.mychild.customView.CustomDialogClass;
import com.mychild.customView.SwitchChildView;
import com.mychild.model.ChildDetailsModel;
import com.mychild.model.ExamModel;
import com.mychild.sharedPreference.ListOfChildrenPreference;
import com.mychild.sharedPreference.PrefManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.webserviceparser.ParentHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParentHomeActivity extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentHomeActivity.class.getSimpleName();

    PrefManager sharedPref;
    ArrayList<String> childrenList = null;
    ArrayList<HashMap<String, String>> childrenGradeAndSection = null;
    CustomDialogClass customDialogue;
    CustomDialogueAdapter customDialogueAdapter = null;
    private TopBar topBar;
    private int selectedposition = 0;
    private SwitchChildView switchChild;
    ListOfChildrenPreference manager;
    private ArrayList<ChildDetailsModel> childList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new PrefManager(this);
        getParentDetailsWebservicescall();
        setContentView(R.layout.activity_parent_home);
        setTopBar();
        switchChildBar();
        setOnClickListener();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Parent Response success");
        Log.i(TAG, responseArray.toString());
        Constants.stopProgress(this);
        //childrenGradeAndSection = ParentHomeJsonParser.getInstance().getChildrenGradeAndSection(responseArray);
        //childrenGradeAndSection = ParentHomeJsonParser.getInstance().getChildrenListwithID(this, responseArray);
        //customDialogueAdapter = new CustomDialogueAdapter(this, childrenGradeAndSection);

        //Storing to Shared preference to cache the child list for the parent
        childList = ParentHomeJsonParser.getInstance().getChildrenListwithID(responseArray);

        if(responseArray != null){
            manager = new ListOfChildrenPreference(this);
            manager.SaveChildrenListToPreference(responseArray);
        }
        else {
            Toast.makeText(this, "No data..",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Parent Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_child:
               if(manager != null){
                   Toast.makeText(this, "Switch Child", Toast.LENGTH_LONG).show();
                   customDialogue = new CustomDialogClass(this);
                   customDialogue.setCancelable(true);
                   customDialogue.show();
               }
            else {
                   Toast.makeText(this, "No Child data found..",Toast.LENGTH_LONG).show();
               }


                break;
            case R.id.homework:
                startActivity(new Intent(ParentHomeActivity.this, ChildHomeWorkActivity.class));
                break;
            case R.id.time_table:
                Toast.makeText(this, "Time Table", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ParentHomeActivity.this, ChildrenTimeTableActivity.class));
                break;
            case R.id.exams:
                //Toast.makeText(this,"Exams",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ExamsActivity.class));
                break;
            case R.id.mail_box:
                startActivity(new Intent(this, ChildInboxActivity.class));
                break;
            case R.id.chat:
                startActivity(new Intent(ParentHomeActivity.this, ParentChatAvtivity.class));
                break;
            case R.id.calender:
                Toast.makeText(this, "Calender", Toast.LENGTH_LONG).show();
                break;

            default:
                //Enter code in the event that that no cases match
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

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.my_child));
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText(sharedPref.getUserNameFromSharedPref());
    }


    public void setOnClickListener() {

        ImageView homeWork = (ImageView) findViewById(R.id.homework);
        ImageView timeTable = (ImageView) findViewById(R.id.time_table);
        ImageView exams = (ImageView) findViewById(R.id.exams);
        ImageView mailBox = (ImageView) findViewById(R.id.mail_box);
        ImageView chat = (ImageView) findViewById(R.id.chat);
        ImageView calender = (ImageView) findViewById(R.id.calender);
        homeWork.setOnClickListener(this);
        timeTable.setOnClickListener(this);
        exams.setOnClickListener(this);
        mailBox.setOnClickListener(this);
        chat.setOnClickListener(this);
        calender.setOnClickListener(this);
        switchChild.switchChildBT.setOnClickListener(this);
    }

    public void getParentDetailsWebservicescall() {
        String Url_parent_details = null;

        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(ParentHomeActivity.this);
            SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if (saredpreferences.contains("UserName")) {
                Url_parent_details = getString(R.string.base_url) + getString(R.string.parent_url_endpoint) + sharedPref.getUserNameFromSharedPref();
                Log.i("===Url_parent===", Url_parent_details);
            }
            WebServiceCall call = new WebServiceCall(ParentHomeActivity.this);
            call.getCallRequest(Url_parent_details);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

}
