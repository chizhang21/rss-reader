package com.cashzhang.ashley;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

/**
 * Created by hadoop on 02/02/2018.
 */

public class MainFragment extends Fragment {

    private final static String TAG = "ashley-rss";
    private static final float PULL_DISTANCE = 0.5F;

    private String[] titleString = new String[] {
            "CS",
            "EE"
    };
    private String[] infoString = new String[] {
            "CS info",
            "EE info"
    };

    private ArrayList<String> listData;

    LListAdapter listAdapter = null;
    ListView listView = null;
    Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
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
        for (int i = 0; i < titleString.length; i ++) {
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
//                Intent intent = new Intent(me_activity, ServiceUpdate.class);
//                me_activity.startService(intent);
                //

                //
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
}
