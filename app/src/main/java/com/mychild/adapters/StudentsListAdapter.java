package com.mychild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import com.mychild.utils.CommonUtils;
import com.mychild.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep on 22-03-2015.
 */
public class StudentsListAdapter extends ArrayAdapter<String> {
    private List<String> list;
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<String> temporaryStorage;

    public StudentsListAdapter(Context context, int resource, List<String> list) {
        super(context, resource, list);
        this.list = list;
        temporaryStorage = new ArrayList<String>();
        temporaryStorage.addAll(list);
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.studentIdTV = (TextView) convertView.findViewById(R.id.student_id_tv);
            holder.studentNameTV = (TextView) convertView.findViewById(R.id.student_name_tv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.studentIdTV.setText("#100" + position + "");
        holder.studentNameTV.setText(getItem(position));

        return convertView;
    }

    private class ViewHolder {
        TextView studentIdTV, studentNameTV;
        CheckBox checkBox;
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    public void getFilters(String str) {
        list.clear();
        if (str.length() == 0) {
            list.addAll(temporaryStorage);
        } else {
            for (String string : temporaryStorage) {
                String name = string.toLowerCase();
                if (name.contains(str)) {
                    list.add(name);
                    CommonUtils.getLogs("Str:::" + str);
                }
            }
        }
        CommonUtils.getLogs("Size :::" + list.size());
        notifyDataSetChanged();
    }
}
