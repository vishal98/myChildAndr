package com.freelancing.connect;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.freelancing.Networkcall.RequestCompletion;
import com.freelancing.Networkcall.WebServiceCall;
import com.freelancing.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity implements RequestCompletion {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final EditText usedID = (EditText) findViewById(R.id.user_id);
        final EditText pwd = (EditText) findViewById(R.id.password);
        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usr = usedID.getText().toString().trim();
                final String password = pwd.getText().toString().trim();
                WebServiceCall call = new WebServiceCall(LoginActivity.this);
                call.LoginRequestApi();

                if (usr.contains(Constants.userID) && password.contains(Constants.pwd)) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
                else if(usr.length()== 0 && password.length() != 0){
                    Constants.showMessage(LoginActivity.this, "Aleart", "User ID cannot be null..");
                }
                else if(usr.length()!= 0 && password.length() == 0){
                    Constants.showMessage(LoginActivity.this, "Aleart", "Password cannot be null..");
                }
                else if (usr != Constants.userID || password != Constants.pwd) {
                    Constants.showMessage(LoginActivity.this, "Aleart", "User ID does not match..");
                }
                else if (password.length()== 0  && password != Constants.pwd) {
                    Constants.showMessage(LoginActivity.this, "Aleart", "Password does not match..");
                }
                else if(usr.length()== 0 && password.length()== 0){
                    Constants.showMessage(LoginActivity.this, "Aleart", "User ID & password cannot be empty...");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestCompletion(JSONObject response) {
        Toast.makeText(this,"onRequestCompletion" ,Toast.LENGTH_LONG).show();
        Log.i("Login",response.toString());
        String role = null;
        try {
            JSONObject loginResponse  = new JSONObject();
            String accessToken =loginResponse.getString("roles");
            JSONArray user = loginResponse.getJSONArray("oles");
            for (int i = 0; i<= user.length();i++){
                JSONObject getRole = user.getJSONObject(i);
                role=getRole.getString("roles");
            }
           Log.i("loginActivity",accessToken+ "," + role);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @Override
    public void onRequestCompletionError(String error) {

    }
}
