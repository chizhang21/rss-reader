/*
package com.cashzhang.nozdormu.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.cashzhang.nozdormu.Constants;
import com.cashzhang.nozdormu.CustomObserver;
import com.cashzhang.nozdormu.FeedlyApi;
import com.cashzhang.nozdormu.FeedlyRequest;
import com.cashzhang.nozdormu.OnNextListener;
import com.cashzhang.nozdormu.RxUtils;
import com.cashzhang.nozdormu.Settings;
import com.cashzhang.nozdormu.bean.CategItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncCatesListService extends IntentService {

    private final static String TAG = SyncCatesListService.class.getSimpleName();

    private static String accessToken = null;

    @Override
    protected void onHandleIntent(Intent intent) {
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

        final FeedlyApi feedlyApi = FeedlyRequest.getInstance();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feedly-Access-Token", accessToken);

        OnNextListener<String> listener = new OnNextListener<String>() {
            @Override
            public void onNext(String categItemString) {
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
                    fos.write(categItemString.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        RxUtils.CustomSubscribe(feedlyApi.getSubsString(headers), new CustomObserver<>(listener));

    }

}




*/
