package com.cashzhang.ashley.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cashzhang.ashley.Constants;
import com.cashzhang.ashley.DialogEditFeed;
import com.cashzhang.ashley.FeedItem;
import com.cashzhang.ashley.MainActivity;
import com.cashzhang.ashley.ObjectIO;
import com.cashzhang.ashley.R;
import com.cashzhang.ashley.Settings;
import com.cashzhang.ashley.adapter.CategListAdapter;
import com.cashzhang.ashley.adapter.FrogAdapter;
import com.cashzhang.ashley.service.SyncCatesListService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cashzhang.ashley.Constants.s_activity;
import static com.cashzhang.ashley.service.ServiceUpdate.ITEM_LIST;

public class CategsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "ashley-rss";

    private ArrayList<String> listLabel;
    private ArrayList<String> listId;

    CategListAdapter listAdapter = null;
    SecCategsFragment secCategsFragment;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.l_list)
    ListView listView;

    public static CategsFragment newInstance() {
        CategsFragment categsFragment = new CategsFragment();
        return categsFragment;
    }

    private final BroadcastReceiver m_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != s_activity) {
                Log.d(TAG, "onReceive: categs fragment");
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
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            activity.registerReceiver(m_broadcastReceiver, new IntentFilter(SyncCatesListService.CATEG_BROADCAST_ACTION));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView: categ fragment");
        View layout = inflater.inflate(R.layout.feed_list, container, false);
        ButterKnife.bind(this, layout);

        mSwipeLayout.setOnRefreshListener(this);
        listAdapter = new CategListAdapter(getActivity());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(itemClickListener);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Constants.getCatesFragmentView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listLabel = new ArrayList<String>();
        listId = new ArrayList<String>();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.feeds_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_feed:
                MainActivity activity = (MainActivity) getActivity();
                Dialog dialog = DialogEditFeed.newInstance(activity, -1);
                dialog.show();
                Log.d(TAG, "add feed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: categ fragment");
        Intent intent = new Intent(getActivity(), SyncCatesListService.class);
        getActivity().startService(intent);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goContentFragment(position);
        }
    };

    private void goContentFragment(int position) {
        Log.d(TAG, "goContentFragment: ");

        secCategsFragment = new SecCategsFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("content", getContent(position));
        secCategsFragment.setArguments(bundle);

        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragmentSwitch(new MainActivity.FragmentSwitch() {
            @Override
            public void gotoFragment(ViewPager viewPager, FrogAdapter adapter) {
                mainActivity.setBundle(bundle);
                viewPager.setCurrentItem(2);
            }
        });
        mainActivity.forSkip();

    }

    private String getContent(int position) {
        return ((listLabel == null) ? null : listLabel.get(position));
    }

    private String getUserID(int position) {
        return ((listId == null) ? null : listId.get(position));
    }

    public void readFromFile() throws IOException, ClassNotFoundException {
        String response;

        File file = new File("data/data/com.cashzhang.ashley/files/categ_list_" + Settings.getId());
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            response = new String(b);
            if (!response.equals("")) {

                if (listLabel != null)
                    listLabel.clear();
                if (listId != null)
                    listId.clear();

                JSONArray jsonArray = JSONArray.parseArray(response);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    listId.add(jsonObject.get("id").toString());
                    listLabel.add(jsonObject.get("label").toString());
                }
                listAdapter.refreshData(listLabel);
            }
        }

        mSwipeLayout.setRefreshing(false);
    }
}

