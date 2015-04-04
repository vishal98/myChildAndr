package com.mychild.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mychild.utils.TopBar;


public class TeacherWriteNewEmailActivity extends BaseActivity implements View.OnClickListener {
    private TopBar topBar;
    private ImageView backButton;
    private EditText mailToEt, mailSubjectET, mailMessageET;
    private Button sendMailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_write_new_email);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.GONE);
        topBar.titleTV.setText(getString(R.string.inbox));
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(this);
        mailToEt = (EditText) findViewById(R.id.mail_to_et);
        mailSubjectET = (EditText) findViewById(R.id.mail_subject_et);
        mailMessageET = (EditText) findViewById(R.id.mail_message_et);

        sendMailBtn = (Button) findViewById(R.id.send_mail_btn);
        sendMailBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.send_mail_btn:
                break;
            default:
        }

    }
}
