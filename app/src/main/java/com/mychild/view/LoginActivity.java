package com.mychild.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
        Log.i(TAG, responseJson.toString());
        String userRole = validatingUser(responseJson);
        Log.i("CompletionuserRole", userRole);
        UpdateUI(userRole);
        Constants.stopProgress(this);
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
        try {
            String accessToken = response.getString("access_token");
            Log.i("loginActivity", accessToken);
            JSONArray user = response.getJSONArray("roles");
            for (int i = 0; i < user.length(); i++) {
                role = user.getString(i);
                Log.i("inside loop", role);
            }
            Log.i("loginActivity", accessToken + "," + role);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return role;
    }
}
