package com.mychild.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;


public class AssignTaskActivity extends BaseActivity implements View.OnClickListener {

    private Spinner classSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        classSpinner = (Spinner) findViewById(R.id.class_spinner);
    }

    @Override
    public void onClick(View v) {

    }
}
