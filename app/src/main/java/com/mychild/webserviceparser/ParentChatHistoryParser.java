package com.mychild.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by MAST_HODC\vramz on 3/30/15.
 */
public class ParentChatHistoryParser {

    public static ParentChatHistoryParser parentChatHistoryParser = null;

    private ParentChatHistoryParser() {

    }

    public static ParentChatHistoryParser getInstance() {
        if (parentChatHistoryParser == null) {
            parentChatHistoryParser = new ParentChatHistoryParser();
        }
        return parentChatHistoryParser;
    }

    public static ArrayList<HashMap<String, String>> getTeacherListForChat(JSONObject jsonObject) {
        ArrayList<HashMap<String, String>> chatArrayList = new ArrayList<HashMap<String, String>>();
        ;
        LinkedHashMap<String, String> chatMap = null;
        try {
            if (jsonObject != null) {
                int size = jsonObject.length();
                for (int i = 0; i < size; i++) {
                    JSONArray conversationArray = jsonObject.getJSONArray("conversations");
                    for (int j = 0; j < conversationArray.length(); j++) {
                        chatMap = new LinkedHashMap<String, String>();
                        JSONObject conversationData = conversationArray.getJSONObject(j);


                        if (conversationData.has("toId")) {
                            String subject = conversationData.getString("toId");
                            chatMap.put("toId", subject);
                        }

                        if (conversationData.has("subject")) {
                            String subject = conversationData.getString("subject");
                            chatMap.put("subject", subject);
                        } else {
                            chatMap.put("subject", "subject");
                        }

                        if (conversationData.has("fromDate")) {
                            String fromDate = conversationData.getString("fromDate");
                            chatMap.put("fromDate", fromDate);
                        }

                        Log.i("childHomeWorkMap", chatMap.toString());
                    }
                    chatArrayList.add(chatMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("chatArrayList", chatArrayList.toString());
        return chatArrayList;
    }
}
