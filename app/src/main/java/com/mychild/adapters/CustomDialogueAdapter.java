package com.mychild.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mychild.model.ChildDetailsModel;
import com.mychild.view.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/25/15.
 */
public class CustomDialogueAdapter extends BaseAdapter {

    public Activity context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,String>> numberOfChild;

    public CustomDialogueAdapter(Activity context,ArrayList<HashMap<String,String>> numberOfChild) {
        this.context = context;
        this.numberOfChild = numberOfChild;
    }

    @Override
    public int getCount() {
        return numberOfChild.size();
    }

    @Override
    public Object getItem(int location) {
        return numberOfChild.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            LayoutInflater inflator = context.getLayoutInflater();
//            view = inflator.inflate(R.layout.custom_dialogue, null);
//            final ViewHolder viewHolder = new ViewHolder();
//            viewHolder.userImage = (ImageView) view.findViewById(R.id.mychild1_image);
//            viewHolder.mychildId = (TextView) view.findViewById(R.id.mychild1_id);
//            viewHolder.mychildName = (TextView) view.findViewById(R.id.mychild1_name);
//            viewHolder.radioBtn = (RadioButton) view.findViewById(R.id.mychild1_check);
//
//            view.setTag(viewHolder);
//            viewHolder.radioBtn.setTag(numberOfChild.get(position));
//        } else {
//            view = convertView;
//            ((ViewHolder) view.getTag()).radioBtn.setTag(numberOfChild.get(position));
//        }
//        ViewHolder holder = (ViewHolder) view.getTag();
//        holder.mychildName.setText(numberOfChild.get(position).get("studentName"));
//        holder.mychildId.setText(numberOfChild.get(position).get("studentId"));
//
//        return view;
//
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.custom_dialogue, null);
        ImageView userImage = (ImageView) convertView.findViewById(R.id.mychild1_image);
        TextView mychildId = (TextView) convertView.findViewById(R.id.mychild1_id);
        TextView mychildName = (TextView) convertView.findViewById(R.id.mychild1_name);
        RadioButton radioBtn = (RadioButton) convertView.findViewById(R.id.mychild1_check);
        radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + numberOfChild.get(position).get("studentName"), Toast.LENGTH_LONG).show();
                ChildDetailsModel childData = new ChildDetailsModel();
                childData.setStudentId(numberOfChild.get(position).get("studentId"));

            }
        });
        mychildName.setText(numberOfChild.get(position).get("studentName"));
        mychildId.setText("#"+numberOfChild.get(position).get("studentId"));
        return convertView;
    }
}
