package com.freelancing.Networkcall;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.freelancing.volley.AppController;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vijay on 3/12/2015.
 */
public class WebServiceCall {

    public static String getToken;
    private static Activity mContext;
    Context context;
    private RequestCompletion mRequestCompletion;

    public WebServiceCall(Activity context) {
        this.mContext = context;
        mRequestCompletion = (RequestCompletion) mContext;
    }


    public void LoginRequestApi() {
        String request_URL = "http://default-environment-8tpprium54.elasticbeanstalk.com/api/login";
        JSONObject headerBodyParam = null;
        LinkedHashMap<String, String> parmKeyValue = new LinkedHashMap<String, String>();
        parmKeyValue.put("username", "test@test.com");
        parmKeyValue.put("password", "test123");
//        headerBodyParam = new JSONObject(parmKeyValue);
        Gson gson = new Gson();
        String parmsToJson = gson.toJson(parmKeyValue);
//        Log.d("parmKeyValue2", parmsToJson);
//        Log.d("parmKeyValue2.1", gson.toJsonTree(parmsToJson).toString());
//        JsonParser parser= new JsonParser();
//        parser.parse(parmsToJson).getAsJsonObject();
//        Log.d("parmKeyValue2.2", new JsonParser().parse(parmsToJson).getAsJsonObject().toString());

        try {
            headerBodyParam = new JSONObject(parmsToJson);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Log.d("parmKeyValue3", headerBodyParam.toString());

        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.POST, request_URL, headerBodyParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // handle response
                            Log.d("JSON Response", response.toString());
                            mRequestCompletion.onRequestCompletion(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleNetworkError(error);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };
            Log.d("Req", req.toString());
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to volley request queue.
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void callRequest(JSONObject object, String url) {
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("JSON Response", response.toString());
                            mRequestCompletion.onRequestCompletion(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleNetworkError(error);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };
            Log.d("Req", req.toString());
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to volley request queue.
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param error volley netwotrk error handling
     */
    public void handleNetworkError(VolleyError error) {

        // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
        // For AuthFailure, you can re login with user credentials.
        // For ClientError, 400 & 401, Errors happening on client side when sending api request.
        // In this case you can check how client is forming the api and debug accordingly.
        // For ServerError 5xx, you can do retry or handle accordingly.
        NetworkResponse response = error.networkResponse;
        String json = null;
        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                case 401:
                    json = new String(response.data);
                    Log.d("Volley error", "" + json);
//                     json = trimMessage(json, "message");
//                     if(json != null) displayMessage(json);
                    break;
            }
        }
        if (error instanceof NetworkError) {
        } else if (error instanceof ClientError) {
            Log.d("ClientError", error.getMessage());
        } else if (error instanceof ServerError) {
            Log.d("ServerError", error.getMessage());
        } else if (error instanceof AuthFailureError) {
            // Log.d("AuthFailureError", error.getMessage());
            mRequestCompletion.onRequestCompletionError("AuthFailureError");
        } else if (error instanceof ParseError) {
            Log.d("ParseError", error.getMessage());
        } else if (error instanceof NoConnectionError) {
            Log.d("NoConnectionError", error.getMessage());
            mRequestCompletion.onRequestCompletionError("Please connect to network...");
        } else if (error instanceof TimeoutError) {
//			Log.d("TimeoutError", error.getMessage());
            mRequestCompletion.onRequestCompletionError("TimeoutError");
        }

    }
}
