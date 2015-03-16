package com.freelancing.connect;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

public class StudentList2 extends ActionBarActivity {
	
	/** String array used as the datasource for the ArrayAdapter of the listview **/
	String[] countries = new String[] {
		"Student1",
		"Student2",
		"Student3",
		"Student4",
		"Student5",
		"Student6"
	};
    ListView list =null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customlist);
        
        /** Defining array adapter to store items for the listview **/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, countries);

        /** Setting the arrayadapter for this listview  **/
        list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);
		
        /** Defining checkbox click event listener **/
		OnClickListener clickListener = new OnClickListener() {			
			@Override
			public void onClick(View v) {
				CheckBox chk = (CheckBox) v;
				int itemCount = list.getCount();
				for(int i=0 ; i < itemCount ; i++){
                    list.setItemChecked(i, chk.isChecked());
				}
			}
		};		
		
		/** Defining click event listener for the listitem checkbox */
		OnItemClickListener itemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CheckBox chk = (CheckBox) findViewById(R.id.check);
				int checkedItemCount = getCheckedItemCount();								
				
				if(list.getCount()==checkedItemCount)
					chk.setChecked(true);
				else
					chk.setChecked(false);
			}
		};		
        
		/** Getting reference to checkbox available in the main.xml layout */ 
        CheckBox chkAll =  ( CheckBox ) findViewById(R.id.check);
        
        /** Setting a click listener for the checkbox **/
        chkAll.setOnClickListener(clickListener);     
        
        /** Setting a click listener for the listitem checkbox **/
        list.setOnItemClickListener(itemClickListener);
        
    }
    
    /**
     * 
     * Returns the number of checked items
     */
    private int getCheckedItemCount(){
    	int cnt = 0;
    	SparseBooleanArray positions = list.getCheckedItemPositions();
    	int itemCount =list.getCount();
    	
    	for(int i=0;i<itemCount;i++){
    		if(positions.get(i))
    			cnt++;
    	}
    	
    	return cnt;
    }
}