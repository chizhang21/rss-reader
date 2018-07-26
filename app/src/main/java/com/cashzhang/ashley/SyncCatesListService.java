package com.cashzhang.ashley;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class SyncCatesListService extends IntentService {
    public static final String BROADCAST_ACTION = "com.cashzhang.syncates.handle";
    public static final String ITEM_LIST = "-categ_list.txt";
    private final static String TAG = "ashley-rss";
    private String tmpTitle = "";

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
    
    public SyncCatesListService() {
        super("SyncCatesListService");
    }


}




