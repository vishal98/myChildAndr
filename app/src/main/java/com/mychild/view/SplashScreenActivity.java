package com.mychild.view;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends BaseActivity {
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Pushbots.sharedInstance().init(SplashScreenActivity.this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        };
        timer = new Timer();
        timer.schedule(task, 2000);                        // timer is scheduled after 2 seconds
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
