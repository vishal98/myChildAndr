package com.mychild.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mychild.view.R;

import java.util.ArrayList;

/**
 * Created by Vijay on 3/25/15.
 */
public class CustomDialogueAdapter extends BaseAdapter {

    protected final Activity context;
    private ArrayList<String> numberOfChild;

    public CustomDialogueAdapter(Activity context,ArrayList<String> numberOfChild) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.custom_dialogue, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.userImage = (ImageView) view.findViewById(R.id.mychild1_image);
            viewHolder.mychildId = (TextView) view.findViewById(R.id.mychild1_id);
            viewHolder.mychildName = (TextView) view.findViewById(R.id.mychild1_name);
            viewHolder.radioBtn = (RadioButton) view.findViewById(R.id.mychild1_check);

            view.setTag(viewHolder);
            viewHolder.radioBtn.setTag(numberOfChild.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).radioBtn.setTag(numberOfChild.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.mychildName.setText(numberOfChild.get(position));

        return view;
    }

    static class ViewHolder {
        protected ImageView userImage;
        protected TextView mychildId;
        protected TextView mychildName;
        protected RadioButton radioBtn;
    }
}
