//package com.mychild.sharedPreference;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by MAST_HODC\vramz on 3/28/15.
// */
//public class ListOfChildrenPreference {
//    // Shared Preferences
//    SharedPreferences pref;
//    // Editor for Shared preferences
//    SharedPreferences.Editor editor;
//    Context context;
//    int PRIVATE_MODE = 0;
//    private static final String RESPONSE = "ChildrenList";
//    public static final String KEY_CHILDREN_LIST = "Childen_List";
//    public ListOfChildrenPreference(Context context){
//
//        this.context = context;
//        pref = this.context.getSharedPreferences(RESPONSE, PRIVATE_MODE);
//        editor = pref.edit();
//    }
//
//    /**
//     *
//     * @param ChildrenList
//     */
//
//    public void SaveChildrenListToPreference( ArrayList<HashMap<String, String>> ChildrenList){
//
//        Log.d("Setchildlist=", ChildrenList.toString());
//        editor.putString(KEY_CHILDREN_LIST,ChildrenList.toString());
//        editor.commit();
//        Log.d("Setchildlist after store", pref.getString(KEY_CHILDREN_LIST, null));
//    }
//
//    /**
//     *
//     * @return ChildrenList .
//     */
//    public ArrayList<HashMap<String,String>> getChildrenListFromPreference(){
//        String userData ;
//        ArrayList<HashMap<String, String>> getChildrenList = new ArrayList<HashMap<String, String>>();
//
//        userData= pref.getString(KEY_CHILDREN_LIST,null);
//
//
//
//
//        Log.d("get UserNAme", pref.getString(KEY_User_Name, null));
//        String getUserName = pref.getString(KEY_User_Name, null);
//        Log.d("Get UserName =", getUserName);
//        return getUserName;
//    }
//}
