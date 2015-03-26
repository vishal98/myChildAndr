package com.mychild.view;

import android.app.Activity;
import android.os.Bundle;

import com.mychild.threads.HttpConnectThread;


/**
 * Created by Sandeep on 17-03-2015.
 */
public class BaseActivity extends Activity {
    public HttpConnectThread httpConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
