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

import com.mychild.model.ExamScheduleModel;
import com.mychild.model.StudentDTO;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.IOnCheckedChangeListener;
import com.mychild.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep on 29-03-2015.
 */
public class ExamsListviewAdapter extends ArrayAdapter<ExamScheduleModel> {
    private List<ExamScheduleModel> list;
    private int resource;
    private LayoutInflater inflater;
    public SparseBooleanArray mSelectedItemsIds;

    public ExamsListviewAdapter(Context context, int resource, List<ExamScheduleModel> list) {
        super(context, resource, list);
        this.list = list;
        mSelectedItemsIds = new SparseBooleanArray();
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.monthTV = (TextView) convertView.findViewById(R.id.month_tv);
            holder.timeTV = (TextView) convertView.findViewById(R.id.time_tv);
            holder.subjectTV = (TextView) convertView.findViewById(R.id.subject_tv);
            holder.weekTV = (TextView) convertView.findViewById(R.id.week_tv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExamScheduleModel examScheduleModel = getItem(position);
        holder.weekTV.setText(CommonUtils.getWeekName(examScheduleModel.getExamsStartTime()));
        holder.subjectTV.setText(examScheduleModel.getSubjectName());
        holder.timeTV.setText(CommonUtils.getTime(examScheduleModel.getExamsStartTime(), examScheduleModel.getExamsEndTime()));
        holder.monthTV.setText(CommonUtils.getMonth(examScheduleModel.getExamsStartTime()));
        holder.dateTV.setText(CommonUtils.getDate(examScheduleModel.getExamsStartTime()));
        //ExamScheduleModel studentDTO = getItem(position);
        holder.checkBox.setTag(getItem(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // int pos = Integer.parseInt((buttonView).getTag().toString());
                StudentDTO dto = (StudentDTO) buttonView.getTag();
                if (isChecked)
                    mSelectedItemsIds.put(dto.getStudentId(), true);
                else
                    mSelectedItemsIds.delete(dto.getStudentId());
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView dateTV, monthTV,timeTV, weekTV, subjectTV;
        CheckBox checkBox;
    }

}
