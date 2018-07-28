package com.cashzhang.ashley;

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

import java.util.HashMap;
import java.util.Map;

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
                //TODO write into categs list file

                //TODO send broadcast
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




