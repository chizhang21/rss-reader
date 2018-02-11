package com.cashzhang.ashley;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static com.cashzhang.ashley.Constants.s_activity;
import static com.cashzhang.ashley.Constants.s_fragmentManager;
import static com.cashzhang.ashley.Constants.s_pullToRefreshLayout;
import static com.cashzhang.ashley.Constants.saveInitialConstants;
import static com.cashzhang.ashley.Constants.saveViews;


public class MainActivity extends AppCompatActivity {

    public List<IndexItem> m_index;
    static final String INDEX = "index.txt";

    private final BroadcastReceiver m_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != s_activity) {
//                AsyncNavigationAdapter.run(s_activity);
                s_pullToRefreshLayout.setRefreshComplete();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveInitialConstants(this);

        // Load the index from file.
        ObjectIO indexReader = new ObjectIO(this, INDEX);
        m_index = (List<IndexItem>) indexReader.read();

        if (null == m_index) {
            m_index = new ArrayList<IndexItem>();
        }


        if (null == savedInstanceState) {
            s_fragmentManager.executePendingTransactions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(m_broadcastReceiver, new IntentFilter(ServiceUpdate.BROADCAST_ACTION));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        saveViews();
    }
}
