package com.mychild.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class LoginActivity extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(LoginActivity.this);
    }


    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Login Response" + responseJson);
        CommonUtils.getLogs("Login Response" + responseArray);
        Log.i(TAG, responseJson.toString());
        String userRole = validatingUser(responseJson);
        Log.i("CompletionuserRole", userRole);
        pushNotification("Activate Notification?", userRole);

    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
        Log.i("Login", error);
    }

    @Override
    public void onClick(View v) {
        final EditText usedID = (EditText) findViewById(R.id.user_id);
        final EditText pwd = (EditText) findViewById(R.id.password);
        if (usedID.getText().length() == 0 && pwd.getText().length() == 0) {
            Constants.showMessage(this, "Sorry", "UserName & Password cannot be empty.");
        } else {
            if (CommonUtils.isNetworkAvailable(this)) {
                CommonUtils.getLogs("Clicked");
                Constants.showProgress(LoginActivity.this);
                WebServiceCall call = new WebServiceCall(LoginActivity.this);
                call.LoginRequestApi(usedID.getText().toString(), pwd.getText().toString());
            } else {
                CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
            }
        }
    }

    public void UpdateUI(String roleUser) {
        if (roleUser.contains("ROLE_PARENT")) {
            Log.i("userRole", roleUser);
            startActivity(new Intent(LoginActivity.this, ParentHomeActivity.class));
            finish();
        } else if (roleUser.contains("ROLE_TEACHER")) {
            startActivity(new Intent(LoginActivity.this, TeacherHomeActivity.class));
            finish();
        }
    }

    public String validatingUser(JSONObject response) {
        String role = null;
        //PrefManager sharedPref = new PrefManager(this);
        try {
            if (response != null) {
                if (response.has("username")) {
                    Log.i("Username", response.getString("username"));
                    //sharedPref.SaveUserNameInInSharedPref(response.getString("username"));
                    StorageManager.writeString(this, getString(R.string.pref_username), response.getString("username"));
                }
                if (response.has("access_token")) {
                    StorageManager.writeString(this, getString(R.string.pref_access_token), response.getString("access_token"));
                }
                JSONArray user = response.getJSONArray("roles");
                for (int i = 0; i < user.length(); i++) {
                    role = user.getString(i);
                    StorageManager.writeString(this, getString(R.string.pref_role), role);
                    Log.i("inside loop", role);
                }
                Log.i("loginActivity", "role =" + role);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return role;
    }

    public void pushNotification(String title, final String role) {
        AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
        alert.setMessage(title);
        alert.setButton(Dialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.getLogs("Possitive Clicked");
                Pushbots.sharedInstance().init(LoginActivity.this);
                CommonUtils.getLogs("Reg ID: " + Pushbots.sharedInstance().regID());
                generateNoteOnSD("Notification_id.txt", Pushbots.sharedInstance().regID());
                UpdateUI(role);
                Constants.stopProgress(LoginActivity.this);
            }
        });
        alert.setButton(Dialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UpdateUI(role);
                Constants.stopProgress(LoginActivity.this);
            }
        });
        alert.show();
    }

    private void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "MyChild");
            CommonUtils.getLogs("Path::" + Environment.getExternalStorageDirectory());
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
