package com.mychild.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import com.mychild.model.StudentDTO;
import com.mychild.utils.IOnCheckedChangeListener;
import com.mychild.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep on 22-03-2015.
 */
public class StudentsListAdapter extends ArrayAdapter<StudentDTO> {
    private List<StudentDTO> list;
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<StudentDTO> temporaryStorage;
    public SparseBooleanArray mSelectedItemsIds;
    private IOnCheckedChangeListener iOnCheckedChangeListener = null;

    public StudentsListAdapter(Context context, int resource, List<StudentDTO> list) {
        super(context, resource, list);
        this.list = list;
        mSelectedItemsIds = new SparseBooleanArray();
        temporaryStorage = new ArrayList<StudentDTO>();
        temporaryStorage.addAll(list);
        this.resource = resource;
        iOnCheckedChangeListener = (IOnCheckedChangeListener) context;
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
        if (mSelectedItemsIds.get(position)) {
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setTag(position);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = Integer.parseInt((buttonView).getTag().toString());
                if (isChecked)
                    mSelectedItemsIds.put(pos, true);
                else
                    mSelectedItemsIds.delete(pos);
                iOnCheckedChangeListener.checkedStateChanged(mSelectedItemsIds.size());
            }
        });
        StudentDTO dto = getItem(position);
        holder.studentIdTV.setText(dto.getStudentId() + "");
        holder.studentNameTV.setText(dto.getStundentName());
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
            for (StudentDTO dto : temporaryStorage) {
                String name = dto.getStundentName().toLowerCase();
                if (name.contains(str)) {
                    list.add(dto);
                }
            }
        }
        notifyDataSetChanged();
    }
}
