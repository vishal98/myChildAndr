package com.mychild.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 4/16/15.
 */
public class TeacherListForChildParser {

    public static TeacherListForChildParser teacherListForChildParser = null;

    private TeacherListForChildParser() {

    }

    public static TeacherListForChildParser getInstance() {
        if (teacherListForChildParser == null) {
            teacherListForChildParser = new TeacherListForChildParser();
        }
        return teacherListForChildParser;
    }

    public ArrayList<HashMap<String, String>> getTeacherList(JSONArray object) {
        ArrayList<HashMap<String, String>> teacherArrayList = new ArrayList<HashMap<String, String>>();
        LinkedHashMap<String, String> teacherMap = null;
            try {
                for (int i = 0; i < object.length(); i++) {
                    teacherMap  = new LinkedHashMap<String, String>();
                    JSONObject listObj = object.getJSONObject(i);
                    if (listObj.has("username")) {
                        String username = listObj.getString("username");
                        teacherMap.put("username", username);
                    }
                    teacherArrayList.add(teacherMap);
                    Log.i("teacherMap", teacherMap.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        Log.i("TeacherArrayList::", teacherArrayList.toString());
        return teacherArrayList;
    }

}
