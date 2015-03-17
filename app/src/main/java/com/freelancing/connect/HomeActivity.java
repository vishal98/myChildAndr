package com.freelancing.connect;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeActivity extends BaseActivity {
    Spinner sp;
    Button selectStudent;
    String msgToStudentText;
    EditText mesToStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        sp = (Spinner) findViewById(R.id.select_subject);
        selectStudent = (Button) findViewById(R.id.select_student);
        mesToStudent = (EditText)findViewById(R.id.texttosend);
//        String spinnerText = sp.getSelectedItem().toString();
        msgToStudentText = mesToStudent.getText().toString();
        selectStudent.setEnabled(false);
        spinnerData();
        mesToStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                enableDisableView();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableDisableView();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(HomeActivity.this,"afterTextChanged",Toast.LENGTH_LONG).show();
                enableDisableView();
            }
        });
        selectStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StudentList2.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    public  void spinnerData(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.subject_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }

    public void enableDisableView(){
        if (!TextUtils.isEmpty(mesToStudent.getText().toString())) {
            // is not empty
            selectStudent.setEnabled(true);
        }
        else {
            // is empty
            selectStudent.setEnabled(false);
        }
    }
}
