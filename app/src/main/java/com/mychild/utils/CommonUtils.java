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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sandeep on 17-03-2015.
 */
public class CommonUtils {
    private static final String TAG = "=====MyChild====";
    private static String[] weeks = {"SUN", "MON", "TUE","WED", "THU","FRI","SAT"};
    private static String[] months = {"JAN", "FEB", "MAR","APR", "MAY","JUN","JUL", "AUG", "SEP","OCT", "NOV","DEC"};
    private static String format = "yyyy-MM-dd hh:mm";
    private static String timeFormat = "hh:mm a";


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
    public static String getWeekName(String str){
        SimpleDateFormat df = new SimpleDateFormat(format);
        int week = 0;
        try {
            Date date = df.parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            week = cal.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weeks[week-1];
    }
    public static String getDate(String str){
        SimpleDateFormat df = new SimpleDateFormat(format);
        int dateNumber = 1;
        try {
            Date date = df.parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            dateNumber = cal.get(Calendar.DATE);
            CommonUtils.getLogs("WEEK:::"+dateNumber);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateNumber+"";
    }
    public static String getMonth(String str){
        SimpleDateFormat df = new SimpleDateFormat(format);
        int month = 0;
        try {
            Date date = df.parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            month = cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return months[month];
    }
    public static String getTime(String str1, String str2){
        SimpleDateFormat df = new SimpleDateFormat(format);
        String[] apPm = {"AM","PM"};
        String month = "";
        try {
            Date date1 = df.parse(str1);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            month = cal1.get(Calendar.HOUR_OF_DAY)+":"+cal1.get(Calendar.MINUTE)+apPm[cal1.get(Calendar.AM_PM)];

            Calendar cal2 = Calendar.getInstance();
            Date date2 = df.parse(str2);
            cal2.setTime(date2);
            month = month+"-"+cal2.get(Calendar.HOUR_OF_DAY)+":"+cal2.get(Calendar.MINUTE)+apPm[cal1.get(Calendar.AM_PM)];
            CommonUtils.getLogs("HOUR:::"+month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month;
        /*String hour1 = "", hour2 = "";
        try {
            DateFormat df = new SimpleDateFormat(timeFormat);
            CommonUtils.getLogs("st1:::"+str1);
            CommonUtils.getLogs("str2:::" +str2);
            hour1 = df.format(str1);
            hour2 = df.format(str2);

            CommonUtils.getLogs("HOur1:::"+hour1);
            CommonUtils.getLogs("WEEK:::" +hour2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hour1+"-"+hour2;*/
    }
}
