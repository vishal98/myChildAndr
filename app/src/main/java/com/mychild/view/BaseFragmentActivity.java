package com.mychild.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mychild.threads.HttpConnectThread;


/**
 * Created by Sandeep on 17-03-2015.
 */
public class BaseFragmentActivity extends FragmentActivity {
    public HttpConnectThread httpConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}
