package com.freelancing.connect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.freelancing.Networkcall.RequestCompletion;
import com.freelancing.Networkcall.WebServiceCall;
import com.freelancing.utils.CommonUtils;
import com.freelancing.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends BaseActivity implements RequestCompletion, View.OnClickListener {

    private EditText usedNameET, passwordET;
    private String request_URL = "http://default-environment-8tpprium54.elasticbeanstalk.com/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usedNameET = (EditText) findViewById(R.id.username_et);
        passwordET = (EditText) findViewById(R.id.password_et);
        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

    }

    @Override
    public void onRequestCompletion(JSONObject response) {
        CommonUtils.getLogs("Login Response : " + response);
        // Toast.makeText(this,"onRequestCompletion" ,Toast.LENGTH_LONG).show();
        Log.i("Login", response.toString());
        String role = null;
        try {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            JSONObject loginResponse = new JSONObject();
            String accessToken = loginResponse.getString("roles");
            JSONArray user = loginResponse.getJSONArray("oles");
            for (int i = 0; i <= user.length(); i++) {
                JSONObject getRole = user.getJSONObject(i);
                role = getRole.getString("roles");
            }
            Log.i("loginActivity", accessToken + "," + role);
            finish();

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    public void onRequestCompletionError(String error) {

    }

    @Override
    public void onClick(View v) {
        final String usr = usedNameET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

       /* if (usr.contains(Constants.userID) && password.contains(Constants.pwd)) {

        }*/
        if (usr.length() == 0 && password.length() != 0) {
            Constants.showMessage(LoginActivity.this, "Aleart", "User ID cannot be null..");
        } else if (usr.length() != 0 && password.length() == 0) {
            Constants.showMessage(LoginActivity.this, "Aleart", "Password cannot be null..");
        /*} else if (usr != Constants.userID || password != Constants.pwd) {
            Constants.showMessage(LoginActivity.this, "Aleart", "User ID does not match..");
        } else if (password.length() == 0 && password != Constants.pwd) {
            Constants.showMessage(LoginActivity.this, "Aleart", "Password does not match..");*/
        } else if (usr.length() == 0 && password.length() == 0) {
            Constants.showMessage(LoginActivity.this, "Aleart", "User ID & password cannot be empty...");
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                WebServiceCall webServiceCall = new WebServiceCall(LoginActivity.this);
                //webServiceCall.LoginRequestApi();
                jsonObject.put("username", usr);
                jsonObject.put("password", password);
                webServiceCall.callRequest(jsonObject, request_URL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
