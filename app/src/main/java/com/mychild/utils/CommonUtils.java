package com.mychild.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mychild.view.R;

/**
 * Created by Sandeep on 17-03-2015.
 */
public class CommonUtils {
    private static final String TAG = "=====MyChild====";

    public static void getLogs(String str) {
        Log.i(TAG, "======" + str + "=====");
    }

    /**
     * This method is used for displaying toast messages, like short time flash messages
     *
     * @param context Activity context object
     * @param str     Message that should be displayed as a toast message.
     */
    public static void getToastMessage(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static Dialog getProgressDialog(Context ctx, String txt) {
        Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        TextView tv = (TextView) dialog.findViewById(R.id.dialog_text);
        tv.setText(txt);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void getErrorLog(String msg) {
        Log.e(TAG, msg);
    }

    /**
     * Checks for network availability.
     *
     * @param cntx Activity context object
     * @return Status of network connection.
     */
    public static boolean isNetworkAvailable(Context cntx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) cntx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
