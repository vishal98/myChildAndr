package com.mychild.view;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.customView.SwitchChildView;
import com.mychild.model.ParentModel;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentWriteMailToTeacher extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentWriteMailToTeacher.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    private Dialog dialog = null;
    ImageView backButton;
    EditText to,subject,message;
    Button sendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_parent_writemail_to_teacher);
        onClickListeners();
        setTopBar();
        switchChildBar();
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
        CommonUtils.getLogs("posting mail Response is success...");
        Log.i(TAG, responseJson.toString());
        String status = getEmailSentStatus(responseJson);
        if(status.contains("success")){
            CommonUtils.getToastMessage(this, "Email Sent.");
        }
        else {
            CommonUtils.getToastMessage(this, "Email Not Sent.");
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
        sendMail = (Button) findViewById(R.id.send_mail_btn);
        to = (EditText) findViewById(R.id.mail_toET);
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
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.parentNameTV.setText("Name");
    }


    public void setSwitchChildDialogueData() {

        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void postEailToServer() {

        if (CommonUtils.isNetworkAvailable(this)) {
            String mailFrom = StorageManager.readString(this, "username", "");
            String mailTo = to.getText().toString();
            String mailSubject = subject.getText().toString();
            String mailMessage = message.getText().toString();

            if (mailTo.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter To Field");
            } else if (mailSubject.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter Subject");
            } else if (mailMessage.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter Message");
            }
            else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("toId", mailTo);
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
