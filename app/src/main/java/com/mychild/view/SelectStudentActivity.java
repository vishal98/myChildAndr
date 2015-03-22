package com.mychild.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mychild.adapters.StudentsListAdapter;
import com.mychild.utils.CommonUtils;

import java.util.ArrayList;


public class SelectStudentActivity extends BaseActivity implements View.OnClickListener {

    private ListView studentsLV;
    private TextView allStudentsTV;
    private EditText searchET;
    StudentsListAdapter studentsListAdapter = null;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);
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
            CommonUtils.getLogs("Watcher : " + s.toString());
            studentsListAdapter.getFilters(s.toString().toLowerCase());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {

    }
}