package com.freelancing.connect;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vijay on 2/27/2015.
 */
public class StudentList extends ActionBarActivity {
    ListView studentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customlist);
        studentList = (ListView) findViewById(R.id.student_list);
        ArrayList<HashMap<String, String>> formList= new ArrayList<HashMap<String, String>>();
        HashMap<String, String> sudentMap = null;
        try {
            JSONObject jsonobject = parseJSON("studentsdetails.json", this);
            JSONArray formArray = (JSONArray) jsonobject.getJSONArray("studentdetails");
            for(int i = 0; i<formArray.length();i++){
                JSONObject jo_inside = formArray.getJSONObject(i);
                String studentName = jo_inside.getString("studentname");
                String studentNo = jo_inside.getString("studentno");
                sudentMap=new HashMap<String, String>();
                sudentMap.put("studentName", studentName);
                sudentMap.put("studentno", studentNo);
                formList.add(sudentMap);
            }
            CustomListAdapter adapter = new CustomListAdapter(this,formList);
            studentList.setAdapter(adapter);

            /** Defining checkbox click event listener **/

            OnClickListener clickListener = new OnClickListener() {

                @Override
                public void onClick(View v) {
//                    CheckBox chk = (CheckBox) v;
                    CheckBox chk = (CheckBox) findViewById(R.id.check);
                    int itemCount = studentList.getCount();
                    for(int i=0 ; i < itemCount ; i++){
                        studentList.setItemChecked(i, chk.isChecked());
                    }
                }
            };
            /** Defining click event listener for the listitem checkbox */
            OnItemClickListener itemClickListener = new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    CheckBox chk = (CheckBox) findViewById(R.id.check);
                    int checkedItemCount = getCheckedItemCount();

                    if(studentList.getCount()==checkedItemCount)
                        chk.setChecked(true);
                    else
                        chk.setChecked(false);
                }
            };
            /** Getting reference to checkbox available in the main.xml layout */
            CheckBox chkAll =  ( CheckBox ) findViewById(R.id.check);
            /** Setting a click listener for the checkbox **/
            chkAll.setOnClickListener(clickListener);
            /** Setting a click listener for the listitem checkbox **/
            studentList.setOnItemClickListener(itemClickListener);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static JSONObject parseJSON(String filename, Context context) throws IOException, JSONException{
        String JSONString = null;
        JSONObject JSONObject = null;
        System.out.println("opening file");
        InputStream is = context.getAssets().open(filename);
        int size = is.available();
        //stores whole data
        System.out.println("reading file");
        byte[] bytes = new byte[size];
        //reader
        is.read(bytes);
        is.close();
        JSONString = new String(bytes, "UTF-8");
        JSONObject = new JSONObject(JSONString);
        return JSONObject;
    }

    /**
     *
     * Returns the number of checked items
     */
    private int getCheckedItemCount(){
        int cnt = 0;
        SparseBooleanArray positions = studentList.getCheckedItemPositions();
        int itemCount = studentList.getCount();

        for(int i=0;i<itemCount;i++){
            if(positions.get(i))
                cnt++;
        }
        return cnt;
    }

}
