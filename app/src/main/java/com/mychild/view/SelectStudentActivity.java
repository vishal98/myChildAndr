package com.mychild.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mychild.adapters.StudentsListAdapter;
import com.mychild.utils.IOnCheckedChangeListener;
import com.mychild.utils.TopBar;

import java.util.ArrayList;


public class SelectStudentActivity extends BaseActivity implements View.OnClickListener, IOnCheckedChangeListener {

    private ListView studentsLV;
    private TextView allStudentsTV;
    private EditText searchET;
    private TopBar topBar;
    StudentsListAdapter studentsListAdapter = null;
    private Button doneBtn;
    private int selectedStudentsSize = 0;
    private final int RESPONSE_CODE = 4321;
    private boolean userSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.assign_task_title));
        studentsLV = (ListView) findViewById(R.id.students_lv);
        allStudentsTV = (TextView) findViewById(R.id.all_students_tv);
        searchET = (EditText) findViewById(R.id.search_et);
        searchET.addTextChangedListener(watcher);
        ArrayList<String> studentNameList = new ArrayList<String>();
        studentNameList.add("Sandeep");
        studentNameList.add("Ravi");
        studentNameList.add("Akhil");
        studentNameList.add("Raju");
        studentNameList.add("Shiva");
        studentNameList.add("Sai");
        studentNameList.add("Santhosh");
        studentNameList.add("Sachin");
        studentNameList.add("Dhoni");
        studentNameList.add("Dravid");
        studentNameList.add("Laxman");
        studentNameList.add("Anil");
        studentNameList.add("Ganguly");
        studentNameList.add("Lara");
        studentsListAdapter = new StudentsListAdapter(this, R.layout.select_student_list_item, studentNameList);
        studentsLV.setAdapter(studentsListAdapter);
        allStudentsTV.append(" (" + studentNameList.size() + ")");

        doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(this);

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            studentsListAdapter.getFilters(s.toString().toLowerCase());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {
        userSelected = true;
        onBackPressed();
    }

    @Override
    public void checkedStateChanged(int size) {
        selectedStudentsSize = size;
        if (size > 0) {
            doneBtn.setVisibility(View.VISIBLE);
        } else {
            doneBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = null;
        if (userSelected) {
            intent = new Intent();
        }
        setResult(RESPONSE_CODE, intent);
        intent = null;
        super.onBackPressed();
    }
}