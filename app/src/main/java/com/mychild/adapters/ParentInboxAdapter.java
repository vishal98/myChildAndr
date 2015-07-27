package com.mychild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mychild.view.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 4/1/15.
 */
public class ParentInboxAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<HashMap<String, String>> parentInbox;


    /**
     * @author Customized List Adapter.
     */
    public ParentInboxAdapter(Context context, ArrayList<HashMap<String, String>> parentInbox) {
        super();
        this.context = context;
        this.parentInbox = parentInbox;

    }

    @Override
    public int getCount() {
        return parentInbox.size();
    }

    @Override
    public Object getItem(int position) {
        return parentInbox.get(position);
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
            convertView = inflater.inflate(R.layout.parent_inbox_adapter, null);
        ImageView inbox = (ImageView) convertView.findViewById(R.id.inboxMessageIV);
        TextView fromTeacher = (TextView) convertView.findViewById(R.id.fromteacherNameTV);
        TextView mailSubject = (TextView) convertView.findViewById(R.id.mailSubjectTV);
        TextView mailDescription = (TextView) convertView.findViewById(R.id.mailDescriptionTV);


        fromTeacher.setText(parentInbox.get(position).get("fromName"));
        mailSubject.setText(parentInbox.get(position).get("title"));
        mailDescription.setText(parentInbox.get(position).get("messageText"));
        return convertView;
    }
}
