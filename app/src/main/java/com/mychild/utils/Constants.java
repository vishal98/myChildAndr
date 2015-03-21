package com.mychild.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
/**
 * Created by vijay on 2/25/2015.
 */
public abstract class Constants {
    public static ProgressDialog progress;
    public static void showMessage(Context context, String title, String message){
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(Dialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
            }
        });
        alert.show();
    }

    public static void showProgress(Context context){
        progress = new ProgressDialog(context);
        progress.setMessage("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
    }
    public static void stopProgress(Context context){
        progress.dismiss();
    }
}
