package com.mychild.customView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.mychild.adapters.CustomDialogueAdapter;
import com.mychild.view.R;


/**
 * Created by Vijay on 3/23/15.
 */
public class CustomDialogClass  extends Dialog  {

    public Activity activity;
    public Dialog dialog;
    public RadioButton mychild1;
    public RadioButton mychild2;
    CustomDialogueAdapter adapter;

    public CustomDialogClass(Activity activity, CustomDialogueAdapter adapter) {
        super(activity);
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listforcustomdialogue);
        ListView childList  = (ListView) findViewById(R.id.childlist);
        childList.setAdapter(adapter);

        childList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

}
