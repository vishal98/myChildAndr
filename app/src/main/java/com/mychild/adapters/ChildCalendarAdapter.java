package com.mychild.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mychild.view.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
public class ChildCalendarAdapter extends BaseAdapter {

    public Activity context;
    private LayoutInflater inflater;
    private JSONArray ary;

    public ChildCalendarAdapter(Activity context,JSONArray ary) {
        this.context = context;
        this.ary = ary;
    }
    @Override
    public int getCount() {
        return ary.length();
    }

 
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.cal_item, null);
        
     JSONObject obj;
	try {
		obj = ary.getJSONObject(position);
		
		TextView subjectTimingTV = (TextView) convertView.findViewById(R.id.timingTV);
        TextView subjectNameTV = (TextView) convertView.findViewById(R.id.subjectTV);
        TextView  descTv = (TextView) convertView.findViewById(R.id.desc);
       
        String startTime= obj.getString("startTime").split(" ")[1];
        String endTime=  obj.getString("endTime").split(" ")[1];
        subjectNameTV.setText(obj.getString("title"));
        descTv.setText(obj.getString("description"));
        if(startTime == "null" && endTime == "null"){
            subjectTimingTV.setText("8:45" +"-"+"9:45");
        }else {
            subjectTimingTV.setText( startTime +"-"+endTime);
        }

        
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

        
        return convertView;

    }
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		try {
			return ary.get(arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
