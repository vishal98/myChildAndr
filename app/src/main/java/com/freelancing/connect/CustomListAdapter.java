package com.freelancing.connect;

/**
 * Created by vijay on 2/26/2015.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class CustomListAdapter extends BaseAdapter {

    protected final Activity context;
    private ArrayList<HashMap<String, String>> studentData;

    public CustomListAdapter(Activity context, ArrayList<HashMap<String, String>> studentData) {
//        super(context, R.layout.customlist, studentData);
        this.context = context;
        this.studentData = studentData;
    }

    @Override
    public int getCount() {
        return studentData.size();
    }

    @Override
    public Object getItem(int location) {
        return studentData.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.customlist, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.select_all);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
//            viewHolder.checkbox
//                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView,
//                                                     boolean isChecked) {
//                            Model element = (Model) viewHolder.checkbox
//                                    .getTag();
//                            element.setSelected(buttonView.isChecked());
//                        }
//                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(studentData.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(studentData.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(studentData.get(position).get("studentName"));
//        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }

}