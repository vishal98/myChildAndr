package com.mychild.Networkcall;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.mychild.sharedPreference.PrefManager;
import com.mychild.utils.CommonUtils;
import com.mychild.view.R;
import com.mychild.volley.AppController;

import org.json.JSONArray;
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
    static PrefManager sharedPref;
    Context context;
    private RequestCompletion mRequestCompletion;

    public WebServiceCall(Activity context) {
        this.mContext = context;
        mRequestCompletion = (RequestCompletion) mContext;
    }


    public void LoginRequestApi(String userName, String Password) {
        String request_URL = mContext.getString(R.string.base_url) + mContext.getString(R.string.login_url_endpoint);
        Log.d("LOGIN URL", request_URL);
        JSONObject headerBodyParam = null;
        LinkedHashMap<String, String> parmKeyValue = new LinkedHashMap<String, String>();
        parmKeyValue.put("username", userName);
        parmKeyValue.put("password", Password);
        Gson gson = new Gson();
        String parmsToJson = gson.toJson(parmKeyValue);
        try {
            headerBodyParam = new JSONObject(parmsToJson);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.POST, request_URL, headerBodyParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseJson) {
                            // handle response
                            Log.d("JSON Response", responseJson.toString());
                            TokenID(responseJson);
                            mRequestCompletion.onRequestCompletion(responseJson, null);
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
            req = new JsonObjectRequest(Request.Method.GET, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseJson) {
                            Log.d("JSON Response", responseJson.toString());
                            mRequestCompletion.onRequestCompletion(responseJson, null);
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

    public void getCallRequest(String url) {

        try {
            JsonArrayRequest req = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("JsonArrayObject", response.toString());
                            mRequestCompletion.onRequestCompletion(null,response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleNetworkError(error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", getToken);
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
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getJsonObjectResponse(String url ) {
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.GET,url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            // handle response
                            Log.d("JsonObject", response.toString());
                            mRequestCompletion.onRequestCompletion(response,null);
                        }
                    }
                    ,new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleNetworkError(error);
                }
            });
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            req.setShouldCache(true);
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(req);
        }
        catch (Exception e) {
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
            mRequestCompletion.onRequestCompletionError("ClientError");
        } else if (error instanceof ServerError) {
            Log.d("ServerError", error.getMessage());
        } else if (error instanceof AuthFailureError) {
            mRequestCompletion.onRequestCompletionError("AuthFailureError");
        } else if (error instanceof ParseError) {
            mRequestCompletion.onRequestCompletionError("ParseError..");
            Log.d("ParseError", error.getMessage());
        } else if (error instanceof NoConnectionError) {
            Log.d("NoConnectionError", error.getMessage());
            mRequestCompletion.onRequestCompletionError("Please connect to network...");
        } else if (error instanceof TimeoutError) {
            mRequestCompletion.onRequestCompletionError("TimeoutError");
        }

    }

    /**
     * Storing the login data in shared preference
     */
    public static String TokenID(JSONObject response) {
        CommonUtils.getLogs("Login Access Token Stored");
        Log.d("Enterted...", "TokenID");
        sharedPref = new PrefManager(mContext);
        try {
            getToken = response.getString("access_token");
            sharedPref.SaveLoginTokenInInSharedPref(getToken);
            Log.d("getToken", getToken);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getToken;
    }

}
