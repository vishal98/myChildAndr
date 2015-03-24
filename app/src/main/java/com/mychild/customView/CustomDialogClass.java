package com.mychild.customView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.mychild.view.R;

/**
 * Created by Vijay on 3/23/15.
 */
public class CustomDialogClass  extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog dialog;
    public RadioButton mychild1;
    public RadioButton mychild2;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogue);
        mychild1 = (RadioButton) findViewById(R.id.mychild1_check);
        mychild1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mychild1_check:
                
                break;

            default:
                break;
        }
        dismiss();
    }

}
