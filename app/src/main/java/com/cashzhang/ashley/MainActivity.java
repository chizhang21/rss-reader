package com.cashzhang.ashley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.cashzhang.ashley.Constants.hideFragments;
import static com.cashzhang.ashley.Constants.s_drawerLayout;
import static com.cashzhang.ashley.Constants.s_fragmentDrawer;
import static com.cashzhang.ashley.Constants.s_fragmentManager;
import static com.cashzhang.ashley.Constants.saveInitialConstants;
import static com.cashzhang.ashley.Constants.setTopOffset;

import com.cashzhang.ashley.Constants;

public class MainActivity extends AppCompatActivity {

    public List<IndexItem> m_index;
    boolean m_showMenuItems = true;
    static final String INDEX = "index.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveInitialConstants(this);

        // Load the index from file.
        ObjectIO indexReader = new ObjectIO(this, INDEX);
        m_index = (List<IndexItem>) indexReader.read();

        if(null == m_index)
        {
            m_index = new ArrayList<IndexItem>();
        }

//        s_fragmentDrawer.setUp(s_drawerLayout);

//        setTopOffset(this);

        if(null == savedInstanceState)
        {
            s_fragmentManager.executePendingTransactions();
        }
    }
}
