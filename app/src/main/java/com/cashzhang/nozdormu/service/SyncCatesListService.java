package com.cashzhang.nozdormu.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cashzhang.nozdormu.Constants;
import com.cashzhang.nozdormu.Settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SyncCatesListService extends IntentService {

    private final static String TAG = "nozdormu";

    public static final String CATEG_BROADCAST_ACTION = "com.cashzhang.syncates.handle";

    private static String accessToken = null;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        accessToken = Settings.getAccessToken();
        if (!accessToken.equals("") && accessToken != null) {
            Log.d(TAG, "token saved in SharedPreferences");
            getCategs();
        }
    }

    public SyncCatesListService() {
        super("SyncCatesListService");
    }

    public void getCategs() {
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String userId = Settings.getId();
                String currentUserCategFile = "data/data/com.cashzhang.nozdormu/files/";
                File file = new File(currentUserCategFile, "categ_list_" + userId);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fos.write(response.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
        StringRequest stringRequest = new StringRequest(Constants.BASE_URL + Constants.CATEGORIES,
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




