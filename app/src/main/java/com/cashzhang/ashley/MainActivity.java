package com.cashzhang.ashley;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import static com.cashzhang.ashley.Constants.s_fragmentManager;
import static com.cashzhang.ashley.Constants.s_swipeLayout;
import static com.cashzhang.ashley.Constants.saveInitialConstants;


public class MainActivity extends AppCompatActivity {

    public List<IndexItem> m_index;
    static final String INDEX = "index.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveInitialConstants(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new MainFragment())
                .commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

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
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (s_swipeLayout.isRefreshing()) {
                    s_swipeLayout.setRefreshing(false);
                    return true;
                } else
                    break;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }
}
