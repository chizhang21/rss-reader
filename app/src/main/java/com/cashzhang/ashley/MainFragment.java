package com.cashzhang.ashley;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.cashzhang.ashley.Constants.s_activity;
import static com.cashzhang.ashley.ServiceUpdate.ITEM_LIST;

/**
 * Created by hadoop on 02/02/2018.
 */

public class MainFragment extends Fragment {

    private final static String TAG = "ashley-rss";
    private static final float PULL_DISTANCE = 0.5F;

    private String[] titleString = new String[]{
            "CS",
            "EE"
    };
    private String[] infoString = new String[]{
            "CS info",
            "EE info"
    };

    private ArrayList<String> listData;

    LListAdapter listAdapter = null;
    ListView listView = null;
    Activity mActivity;

    private final BroadcastReceiver m_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != s_activity) {
                Log.d(TAG, "onReceive:");
                try {
                    readFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        activity.registerReceiver(m_broadcastReceiver, new IntentFilter(ServiceUpdate.BROADCAST_ACTION));
        Log.d(TAG, "onAttach: ");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView: ");
        View layout = inflater.inflate(R.layout.feed_list, container, false);
//        TextView emptyView = (TextView) layout.findViewById(R.id.empty_text);
//        emptyView.setText(R.string.empty_manage_list_view);

        listView = (ListView) layout.findViewById(R.id.l_list);

        listData = new ArrayList<String>();
        for (int i = 0; i < titleString.length; i++) {
            Log.d(TAG, "onCreate: listData add: " + titleString[i]);
            listData.add(titleString[i]);
        }
        listAdapter = new LListAdapter(mActivity, listData);
        listView.setAdapter(listAdapter);
        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.feeds_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity me_activity = (MainActivity) getActivity();
        switch (item.getItemId()) {
            case R.id.add_feed:
                MainActivity activity = (MainActivity) getActivity();
                Dialog dialog = DialogEditFeed.newInstance(activity, -1);
                dialog.show();
                Log.d(TAG, "add feed: ");
                return true;
            case R.id.refresh:
                Intent intent = new Intent(me_activity, ServiceUpdate.class);
                me_activity.startService(intent);
                //TODO

                Log.d(TAG, "refresh: ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void readFromFile() throws IOException, ClassNotFoundException {

        ObjectIO reader = new ObjectIO(getActivity(), MainActivity.INDEX);
        Iterable<IndexItem> indexItems = (Iterable<IndexItem>) reader.read();

        for (IndexItem indexItem : indexItems) {
            try {
                readKeySet(Long.toString(indexItem.m_uid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readKeySet(String uid) {
        Log.d(TAG, "readKeySet: uid = " + uid);

        ObjectIO reader = new ObjectIO(getActivity(), uid + ITEM_LIST);
        ObjectIO mapReader = new ObjectIO(getActivity(), uid);

        HashSet<Long> set = null;
        try {
            set = (HashSet<Long>) reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "readKeySet set: " + set.toString());
        Log.d(TAG, "readKeySet size: " + set.size());

        TreeMap<Long, FeedItem> mapFromFile = null;
        try {
            mapFromFile = (TreeMap<Long, FeedItem>) mapReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Object obj : set) {
            Log.d(TAG, "readKeySet value: " + mapFromFile.get(obj).m_title.toString());
        }
    }
}

