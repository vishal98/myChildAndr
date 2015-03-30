package com.mychild.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mychild.model.ExamModel;
import com.mychild.view.R;

import java.util.List;

/**
 * Created by Sandeep on 30-03-2015.
 */
public class ExamsTypesListviewAdapter extends ArrayAdapter<ExamModel> {
    private List<ExamModel> list;
    private int resource;
    private LayoutInflater inflater;
    public SparseBooleanArray mSelectedItemsIds;
    private int examId;

    public ExamsTypesListviewAdapter(Context context, int resource, List<ExamModel> list, int examId) {
        super(context, resource, list);
        this.list = list;
        mSelectedItemsIds = new SparseBooleanArray();
        this.resource = resource;
        this.examId = examId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.examTypeTV = (TextView) convertView.findViewById(R.id.exam_type_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExamModel examModel = getItem(position);
        holder.examTypeTV.setText(examModel.getExamType());
        holder.dateTV.setText("23 Mar - 05 April 2015");
        holder.checkBox.setTag(getItem(position));
        if (examId == Integer.parseInt(examModel.getExamId())) {
            holder.checkBox.setChecked(true);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView examTypeTV, dateTV;
        CheckBox checkBox;
    }
}
