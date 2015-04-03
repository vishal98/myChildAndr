package com.mychild.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.mychild.interfaces.AsyncTaskInterface;
import com.mychild.threads.HttpConnectThread;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.TopBar;


public class TeacherEmailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AsyncTaskInterface {
    private TopBar topBar;
    private ImageView writeMail;
    private EditText searchET;
    private ListView emailsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_emails);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.inbox));
        writeMail = (ImageView) findViewById(R.id.write_mail_iv);
        writeMail.setOnClickListener(this);
        searchET = (EditText) findViewById(R.id.search_et);
        searchET.addTextChangedListener(watcher);
        emailsLV = (ListView) findViewById(R.id.emails_lv);
        String url = getString(R.string.base_url) + getString(R.string.url_teacher_email);
        httpConnectThread = new HttpConnectThread(this, null, this);
        httpConnectThread.execute(url);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.write_mail_iv:
                Intent intent = new Intent(this, TeacherWriteNewEmailActivity.class);
                startActivity(intent);
                intent = null;
                break;
            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        CommonUtils.getLogs("Response::::" + object);
    }


}
