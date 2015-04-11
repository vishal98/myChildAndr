package com.mychild.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ParentModel;
import com.mychild.sharedPreference.StorageManager;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.TopBar;
import com.mychild.volley.AppController;

/**
 * Created by Vijay on 4/11/15.
 */
public class ParentMailDetailedActivity extends BaseFragmentActivity implements View.OnClickListener, IOnSwichChildListener {
    public static final String TAG = ParentWriteMailToTeacher.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    ImageView backButton;
    private Dialog dialog = null;
    TextView detailedMailTV,regardsFromTV, mailTimeTV,mailTitleTV,mailFromTV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_parent_detailed_inbox);
        setOnClickListeners();
        switchChildBar();
        setTopBar();
        UpdateUI();
    }
    @Override
    public void onSwitchChild(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.write_mailIV:
                startActivity(new Intent(this, ParentWriteMailToTeacher.class));
                break;
            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }

            default:
                //Enter code in the event that that no cases match
        }

    }

    public void setOnClickListeners(){
        backButton = (ImageView) findViewById(R.id.back);
        detailedMailTV = (TextView) findViewById(R.id.mail_details);
        regardsFromTV = (TextView) findViewById(R.id.regards_from);
        mailTimeTV = (TextView) findViewById(R.id.mailTimeTV);
        mailTitleTV = (TextView) findViewById(R.id.mailTitleTV);
        mailFromTV = (TextView) findViewById(R.id.mailFromTV);
        backButton.setOnClickListener(this);

    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.INVISIBLE);
        topBar.titleTV.setText(getString(R.string.inbox));
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        StorageManager.readString(this, "username", "");
        switchChild.parentNameTV.setText(StorageManager.readString(this, "username", ""));
    }


    public void UpdateUI(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String mailFrom = extras.getString("mailFrom");
            String mailDescription = extras.getString("mailDescription");
            String mailTitle = extras.getString("mailTitle");

            detailedMailTV.setText(mailDescription);
            mailFromTV.setText("From :"+mailFrom);
            regardsFromTV.setText(mailFrom);
            mailTitleTV.setText(mailTitle);
        }

    }


}
