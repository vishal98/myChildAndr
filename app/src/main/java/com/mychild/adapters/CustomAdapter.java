package com.mychild.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mychild.view.R;

import java.util.List;


public class CustomAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private List<String> timeList;
    private Typeface typeface;

    /**
     * Constructor
     *
     * @param context  activity context
     * @param resource layout resource
     * @param objects  list of drop down items
     */
    public CustomAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        timeList = objects;
        //	this.typeface = typeface;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * This method is used load drop down
     *
     * @param position    position in drop down list
     * @param convertView View of drop down item
     * @param parent      It is ViewGroup of drop down
     * @return returns the drop down item view
     */
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.drop_down_item, parent, false);
        TextView spinnerTV = (TextView) row.findViewById(R.id.drop_down_item);
        //spinnerTV.setTypeface(typeface);
        spinnerTV.setText(timeList.get(position));
        return row;

    }
}
