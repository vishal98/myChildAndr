package com.freelancing.Networkcall;

import org.json.JSONObject;



public interface RequestCompletion {
	
	public void onRequestCompletion(JSONObject response);
	public void onRequestCompletionError(String error);


}
