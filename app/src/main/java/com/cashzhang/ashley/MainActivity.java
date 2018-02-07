package com.cashzhang.ashley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.cashzhang.ashley.Constants.saveInitialConstants;

public class MainActivity extends AppCompatActivity {

    public List<IndexItem> m_index;
    boolean m_showMenuItems = true;
    static final String READ_ITEMS = "read_items.txt";
    static final String INDEX = "index.txt";
    static final String FAVOURITES = "favourites.txt";

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

        // Load the read items to the tags Adapter.
        ObjectIO readItemReader = new ObjectIO(this, READ_ITEMS);
        Collection<Long> readItemsFromFile = (Collection<Long>) readItemReader.read();

        if(null != readItemsFromFile)
        {
            mReadItemTimes.addAll(readItemsFromFile);
        }

        s_fragmentDrawer.setUp(s_drawerLayout);

        setTopOffset(this);

        if(null == savedInstanceState)
        {
            // Create and hide the fragments that go inside the content frame.
            if(!canFitTwoFragments())
            {
                hideFragments(s_fragmentWeb);
            }

            hideFragments(s_fragmentFavourites, s_fragmentManage, s_fragmentSettings);
            s_fragmentManager.executePendingTransactions();
        }
    }
}
