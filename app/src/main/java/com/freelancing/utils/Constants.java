package com.freelancing.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.app.Dialog;
import android.content.DialogInterface;
/**
 * Created by vijay on 2/25/2015.
 */
public abstract class Constants {

    public static String userID = "myschool".trim();
    public static String pwd = "123456".trim();

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
}
