package com.mychild.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mychild.view.R;

/**
 * Created by Sandeep on 25-03-2015.
 */
public class TopBar extends RelativeLayout {
    public ImageView backArrowIV,mMenuSliderIV;
    public TextView titleTV;
    public ImageView logoutIV;

    public TopBar(Context context) {
        super(context);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initTopBar() {
        inflateHeader();
    }

    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.topbar, this);
        backArrowIV = (ImageView) findViewById(R.id.back_arrow_iv);
        titleTV = (TextView) findViewById(R.id.title_tv);
        logoutIV = (ImageView) findViewById(R.id.logoutIV);
      //  mMenuSliderIV=(ImageView) findViewById(R.id.menusliderimgview);

    }
}
