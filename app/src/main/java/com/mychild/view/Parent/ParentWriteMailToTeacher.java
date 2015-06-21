package com.mychild.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.TeacherListForChildAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.model.ParentModel;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.view.CommonToApp.BaseFragmentActivity;
import com.mychild.view.CommonToApp.LoginActivity;
import com.mychild.view.R;
import com.mychild.volley.AppController;
import com.mychild.webserviceparser.TeacherListForChildParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentWriteMailToTeacher extends BaseFragmentActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentWriteMailToTeacher.class.getSimpleName();
    private TopBar topBar;
    //private SwitchChildView switchChild;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    private Dialog dialog = null;
    private ImageView backButton;
    private EditText subject,message;
    private AutoCompleteTextView to;
    private ImageView sendMail;
    String responseType;
    private ArrayAdapter<String> arrayAdapter;
    public static List<String> ll ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setSwitchChildDialogueData();
        setContentView(R.layout.activity_parent_writemail_to_teacher);
        onClickListeners();
        setTopBar();
      //  switchChildBar();
        getTeacherList();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.send_mail_btn:
                Constants.showProgress(this);
                postEailToServer();
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
                //Enter code in the event that that no cases match
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.mail_toET);
        ll = new ArrayList<String>();
        String status = null;
     //   String[] countries=null;
        if(responseType == "JSONARRAY"){
            CommonUtils.getLogs("get teacher Response is success...");
            if(responseArray!=null && responseArray.length()>0) {
                Log.i(TAG, responseArray.toString());
                ArrayList<HashMap<String, String>> teacherListForChild = TeacherListForChildParser.getInstance().getTeacherList(responseArray);
                for (int i = 0; i < teacherListForChild.size(); i++) {
                    ll.add(teacherListForChild.get(i).get("username").toString());
                    Log.e("--------",teacherListForChild.get(i).get("username").toString());
                }
            }
            arrayAdapter = new DropDownAdapter(this,
                    R.layout.arealist_suggestion, R.id.searchdropdown,
                    ll);
            textView.setThreshold(1);
            textView.setAdapter(arrayAdapter);
        }
        else {
            CommonUtils.getLogs("posting mail Response is success...");
            Log.i(TAG, responseJson.toString());
            status = getEmailSentStatus(responseJson);
            if(status.contains("success")){
                CommonUtils.getToastMessage(this, "Email Sent.");
            }
            else {
                CommonUtils.getToastMessage(this, "Email Not Sent.");
            }
        }
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("posting mail Failure....");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);

    }

    public void onClickListeners(){
        backButton = (ImageView) findViewById(R.id.back);
        sendMail = (ImageView) findViewById(R.id.send_mail_btn);
        to = (AutoCompleteTextView ) findViewById(R.id.mail_toET);
        subject = (EditText) findViewById(R.id.mail_subjectET);
        message = (EditText) findViewById(R.id.mail_messageET);
        backButton.setOnClickListener(this);
        sendMail.setOnClickListener(this);

    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.INVISIBLE);
        topBar.titleTV.setText(getString(R.string.inbox));
        topBar.logoutIV.setOnClickListener(this);
    }

    public void switchChildBar() {
       // switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        //switchChild.initSwitchChildBar();
        //switchChild.childNameTV.setText("Name");
    }


    public void setSwitchChildDialogueData() {

        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public  void getTeacherList(){
        responseType = "JSONARRAY";
        Constants.showProgress(this);
        String Url_teacher_list = null;
        if (CommonUtils.isNetworkAvailable(this)) {

            Url_teacher_list = getString(R.string.base_url) + "/app/parent/getUsers/1";
                Log.i("===list_teacher===", Url_teacher_list);
            WebServiceCall call = new WebServiceCall(this);
            call.getCallRequest(Url_teacher_list);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    public void postEailToServer() {
        responseType = "JSONOBJECT";
        if (CommonUtils.isNetworkAvailable(this)) {
            String mailFrom = StorageManager.readString(this, "username", "");

            String mailTo = to.getText().toString();
           // String mailToString[] = mailTo.split("=");
            Log.i("----->123",mailTo);
            String mailToStringdata = to.getText().toString();
            Log.i("----->1234",mailToStringdata);

            String mailSubject = subject.getText().toString();
            String mailMessage = message.getText().toString();
            if (mailTo.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter To Field");
                Constants.stopProgress(this);
            } else if (mailSubject.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter Subject");
                Constants.stopProgress(this);
            } else if (mailMessage.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter Message");
                Constants.stopProgress(this);
            }
            else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("toId", mailToStringdata);
                    jsonObject.put("fromId", mailFrom);
                    jsonObject.put("title", mailSubject);
                    jsonObject.put("messageText", mailMessage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommonUtils.getLogs("EMAIL POST PARENT ::" + jsonObject);
                String emailPostURL = getString(R.string.base_url)+getString(R.string.parent_email_to_teahcer);
                WebServiceCall webServiceCall = new WebServiceCall(this);
                webServiceCall.postToServer(jsonObject, emailPostURL);
            }
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }

    }


    public String getEmailSentStatus(JSONObject response){
        String mailSentStatus = null;
        try {
            if (response.has("status")) {
               mailSentStatus = response.getString("status");
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        CommonUtils.getLogs(mailSentStatus);
        return mailSentStatus;
    }

}
