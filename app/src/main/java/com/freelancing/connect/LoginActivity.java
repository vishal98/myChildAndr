package com.freelancing.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final EditText usedID = (EditText) findViewById(R.id.user_id);
        final EditText pwd = (EditText) findViewById(R.id.password);
         login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(usedID.getText().length()== 0 && pwd.getText().length() != 0){
                Constants.showProgress(LoginActivity.this);
                WebServiceCall call = new WebServiceCall(LoginActivity.this);
                call.LoginRequestApi(usedID.getText().toString(),pwd.getText().toString());
                //}

//                if (usr.contains(Constants.userID) && password.contains(Constants.pwd)) {
//                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                }
//                else if(usr.length()== 0 && password.length() != 0){
//                    Constants.showMessage(LoginActivity.this, "Aleart", "User ID cannot be null..");
//                }
//                else if(usr.length()!= 0 && password.length() == 0){
//                    Constants.showMessage(LoginActivity.this, "Aleart", "Password cannot be null..");
//                }
//                else if (usr != Constants.userID || password != Constants.pwd) {
//                    Constants.showMessage(LoginActivity.this, "Aleart", "User ID does not match..");
//                }
//                else if (password.length()== 0  && password != Constants.pwd) {
//                    Constants.showMessage(LoginActivity.this, "Aleart", "Password does not match..");
//                }
//                else if(usr.length()== 0 && password.length()== 0){
//                    Constants.showMessage(LoginActivity.this, "Aleart", "User ID & password cannot be empty...");
//                }
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


        Toast.makeText(this, "onRequestCompletion", Toast.LENGTH_LONG).show();
        Log.i("Login",response.toString());
        String userRole = validatingUser(response);
        Log.i("CompletionuserRole",userRole);
        UpdateUI(userRole);
        Constants.stopProgress(this);


    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        Toast.makeText(this, "IN-Valid user login", Toast.LENGTH_LONG).show();
        Log.i("Login",error);

    }

    public void UpdateUI(String roleUser){

        if(roleUser == "ROLE_USER"){
            Log.i("userRole",roleUser);
            Intent intentHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intentHomeActivity);
        }

    }

    public String validatingUser(JSONObject response){
        String role = null;
        try {
            String accessToken =response.getString("access_token");
            Log.i("loginActivity",accessToken);
            JSONArray user = response.getJSONArray("roles");
            System.out.println("user =" + user.length());
            for (int i = 0; i<user.length();i++){
                role=user.getString(i);
                Log.i("inside loop",role);
            }
            Log.i("loginActivity",accessToken+ "," + role);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return role;
    }
}
