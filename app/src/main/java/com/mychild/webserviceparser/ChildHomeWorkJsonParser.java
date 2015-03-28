package com.mychild.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 3/27/15.
 */
public class ChildHomeWorkJsonParser {
    public static ChildHomeWorkJsonParser childHomeWorkJsonParser = null;

    private ChildHomeWorkJsonParser() {

    }

    public static ChildHomeWorkJsonParser getInstance() {
        if (childHomeWorkJsonParser == null) {
            childHomeWorkJsonParser = new ChildHomeWorkJsonParser();
        }
        return childHomeWorkJsonParser;
    }

    public static ArrayList<HashMap<String, String>> getChildrenHomework(JSONArray jsonArray) {
        ArrayList<HashMap<String, String>> childHomeWork = null;
        LinkedHashMap<String, String> childHomeWorkMap = null;
        try {
            if (jsonArray != null) {
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.i("homeworkList", object.getString("homeworkList"));
                    childHomeWork =  new ArrayList<HashMap<String, String>>();

                    if(object.has("homeworkList")){
                        for (int j = 0 ;j < object.length();j++){
                            childHomeWorkMap = new LinkedHashMap<String, String>();
                            JSONObject childName = object.getJSONObject("homeworkList");
                            if (childName.has("homework")) {
                                String homeworkName = childName.getString("homework");
                                childHomeWorkMap.put("homework",homeworkName);
                            }
                            if (childName.has("subject")) {
                                String homeworkSubject = childName.getString("subject");
                                childHomeWorkMap.put("subject",homeworkSubject);
                            }
                            if (childName.has("message")) {
                                String homeworkMessage = childName.getString("message");
                                childHomeWorkMap.put("message",homeworkMessage);
                            }
                            Log.i("childHomeWorkMap", childHomeWorkMap.toString());
                            childHomeWork.add(childHomeWorkMap);
                            Log.i("childHomeWorkArrayList", childHomeWork.toString());
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("childHomeWorkArrayList", childHomeWork.toString());
        return childHomeWork;
    }


}
