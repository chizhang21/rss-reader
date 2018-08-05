package com.cashzhang.ashley.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cashzhang.ashley.Constants;
import com.cashzhang.ashley.DialogEditFeed;
import com.cashzhang.ashley.FeedItem;
import com.cashzhang.ashley.IndexItem;
import com.cashzhang.ashley.MainActivity;
import com.cashzhang.ashley.ObjectIO;
import com.cashzhang.ashley.R;
import com.cashzhang.ashley.Settings;
import com.cashzhang.ashley.adapter.CategListAdapter;
import com.cashzhang.ashley.adapter.FrogAdapter;
import com.cashzhang.ashley.bean.CategItem;
import com.cashzhang.ashley.service.ServiceUpdate;
import com.cashzhang.ashley.service.SyncCatesListService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cashzhang.ashley.Constants.s_activity;
import static com.cashzhang.ashley.service.ServiceUpdate.ITEM_LIST;


public class SecCategsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "ashley-rss";

    private ArrayList<String> listTitle;
    private ArrayList<String> listFeedId;
    Bundle bundle;
    CategListAdapter listAdapter = null;
    MainFragment mainFragment;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.l_list)
    ListView listView;

    private String label;

    public static SecCategsFragment newInstance() {
        SecCategsFragment secCategsFragment = new SecCategsFragment();
        return secCategsFragment;
    }

    private final BroadcastReceiver m_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != s_activity) {
                Log.d(TAG, "onReceive: sec categs fragment");
                try {
                    readFromFile(label+".cif");
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

        Log.d(TAG, "onCreateView: ");
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
        Constants.getSecCatesFragmentView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listFeedId = new ArrayList<String>();
        listTitle = new ArrayList<String>();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.feeds_menu, menu);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint() -> isVisibleToUser: " + isVisibleToUser);
        if (isVisibleToUser) {
            try {
                loadData(MainActivity.getBundle());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            onRefresh();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onRefresh() {
        try {
            readFromFile(label+".cif");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goContentFragment(position);
        }
    };

    private void goContentFragment(int position) {
        Log.d(TAG, "goContentFragment: ");

        mainFragment = new MainFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("feed_id", getFeedId(position));
        mainFragment.setArguments(bundle);

        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragmentSwitch(new MainActivity.FragmentSwitch() {
            @Override
            public void gotoFragment(ViewPager viewPager, FrogAdapter adapter) {
                mainActivity.setBundle(bundle);
                viewPager.setCurrentItem(3);
            }
        });
        mainActivity.forSkip();

    }
    private String getFeedId(int position) {
        return ((listFeedId == null) ? null : listFeedId.get(position));
    }

    public void loadData(Bundle tmpBundle) throws IOException, ClassNotFoundException {
        try {
            bundle = tmpBundle;
            Log.d(TAG, "loadData: bundle == null? " + (bundle == null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            label = bundle.getString("categ_lebel");
            Log.d(TAG, "SecFragm: bundle != null, label == " + label);
            if (label != null) {
                File src = new File("data/data/com.cashzhang.ashley/files/" + label + ".cif");
                File dst = new File("data/data/com.cashzhang.ashley/files/tmp.cif");
                if (!dst.exists()) {
                    try {
                        dst.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                copyLabelFileToDefault(src, dst);
                readFromFile(label+".cif");
            } else
                readFromFile("tmp.cif");
        }
    }

    private void copyLabelFileToDefault(File src, File dst) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(src).getChannel();
            outputChannel = new FileOutputStream(dst).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    public void readFromFile(String fileLabelName) throws IOException, ClassNotFoundException {
        String response;

        File file = new File("data/data/com.cashzhang.ashley/files/" + fileLabelName);
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            response = new String(b);
            if (!response.equals("")) {

                if (listTitle != null)
                    listTitle.clear();
                if (listFeedId != null)
                    listFeedId.clear();


                JSONArray jsonArray = JSONArray.parseArray(response);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    listFeedId.add(jsonObject.get("id").toString());
                    listTitle.add(jsonObject.get("title").toString());
                }
                listAdapter.refreshData(listTitle);
            }
        }
        mSwipeLayout.setRefreshing(false);
    }

}

