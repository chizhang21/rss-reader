package com.cashzhang.ashley.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cashzhang.ashley.Constants;
import com.cashzhang.ashley.Settings;
import com.cashzhang.ashley.bean.Categ;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okio.Utf8;

public class SyncCatesListService extends IntentService {

    private final static String TAG = "ashley-rss";
    private static final String BASE_URL = "https://cloud.feedly.com";
    private static final String CATEGORIES = "/v3/categories?sort=feedly";

    public static final String CATEG_BROADCAST_ACTION = "com.cashzhang.syncates.handle";
    public static final String ITEM_LIST = "-categ_list.txt";

    private static String accessToken = null;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: sync categs");
        accessToken = Settings.getAccessToken();
        //TODO call sync categ method
        if (!accessToken.equals("") && accessToken != null) {
            Log.d(TAG, "token saved in SharedPreferences");
            getCategs();
        }
    }

    public SyncCatesListService() {
        super("SyncCatesListService");
    }

    public void getCategs() {
        Log.d(TAG, "sync categ service, start get categs");
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "CATEGS: " + response);
                String userId = Settings.getId();
                String currentUserCategFile = "data/data/com.cashzhang.ashley/files/";
                File file = new File(currentUserCategFile, "categ_list_" + userId);
                if (!file.exists()) {
                    try {
                        file.createNewFile();

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(response.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                //send broadcast
                Intent broadcast = new Intent(CATEG_BROADCAST_ACTION);
                sendBroadcast(broadcast);
                stopSelf();

            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        StringRequest stringRequest = new StringRequest(BASE_URL + CATEGORIES,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", accessToken);
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

}




