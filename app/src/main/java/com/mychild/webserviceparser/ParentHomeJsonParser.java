package com.mychild.webserviceparser;

import android.util.Log;

import com.mychild.model.ParentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vijay on 3/21/15.
 */
public class ParentHomeJsonParser  {
    public static ParentHomeJsonParser parentHomeJsonParser = null;

    private ParentHomeJsonParser() {

    }

    public static ParentHomeJsonParser getInstance() {
        if (parentHomeJsonParser == null) {
            parentHomeJsonParser = new ParentHomeJsonParser();
        }
        return parentHomeJsonParser;
    }

    public static ArrayList<String> getChildrenList(JSONArray jsonArray) {
        ArrayList<String> childrenArrayList = null;
        if (jsonArray != null) {
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                try {
                    ParentModel parentModel = new ParentModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONArray childrenArray = object.getJSONArray("children");
                    childrenArrayList = new ArrayList<String>();
                    for(int j= 0; j<childrenArray.length();j++ ){
                        JSONObject noOfStudent = childrenArray.getJSONObject(j);
                        if (noOfStudent.has("studentName")) {
                            childrenArrayList.add(noOfStudent.getString("studentName"));
                        }
                    }
                    parentModel.setNumberOFChild(childrenArrayList);
                    parentModel = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("StudentName", childrenArrayList.toString());
        return childrenArrayList;
    }
}
