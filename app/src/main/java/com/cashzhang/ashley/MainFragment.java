package com.cashzhang.ashley;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cashzhang.ashley.Constants.s_activity;
import static com.cashzhang.ashley.ServiceUpdate.ITEM_LIST;

/**
 * Created by hadoop on 02/02/2018.
 */

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "ashley-rss";

    private ArrayList<String> listTitle;
    private ArrayList<String> listData;
    private ArrayList<String> listUrl;
    private ArrayList<String> listContent;
    private ArrayList<String> listTContent;
    private ArrayList<String> listTime;

    LListAdapter listAdapter = null;
    ContentFragment contentFragment;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.l_list)
    ListView listView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
//    private SwipeRefreshLayout mSwipeLayout;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            activity.registerReceiver(m_broadcastReceiver, new IntentFilter(ServiceUpdate.BROADCAST_ACTION));
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
        listAdapter = new LListAdapter(getActivity());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(itemClickListener);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Constants.getFragmentView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listTitle = new ArrayList<String>();
        listData = new ArrayList<String>();
        listUrl = new ArrayList<String>();
        listContent = new ArrayList<String>();
        listTContent = new ArrayList<String>();
        listTime = new ArrayList<String>();
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
        Intent intent = new Intent(getActivity(), ServiceUpdate.class);
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
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        contentFragment = new ContentFragment();

        Bundle bundle = new Bundle();
        bundle.putString("title", getTitle(position));
        bundle.putString("time", getTime(position));
        bundle.putString("url", getUrl(position));
        bundle.putString("content", getContent(position));

        contentFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, contentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private String getTitle(int position) {
        return ((listData == null) ? null : listData.get(position));
    }
    private String getTime(int position) {
        return ((listTime == null) ? null : listTime.get(position));
    }
    private String getUrl(int position) {
        return ((listUrl == null) ? null : listUrl.get(position));
    }
    private String getContent(int position) {
        return ((listContent == null) ? null : listContent.get(position));
    }

    /* String Data Long */
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType);
        String strTime = dateToString(date, formatType);
        return strTime;
    }

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime);
        String sDateTime = dateToString(dateOld, formatType);
        Date date = stringToDate(sDateTime, formatType);
        return date;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /* String Data Long */

    public void readFromFile() throws IOException, ClassNotFoundException {

        ObjectIO reader = new ObjectIO(getActivity(), MainActivity.INDEX);
        Iterable<IndexItem> indexItems = (Iterable<IndexItem>) reader.read();

        if (indexItems != null) {
            for (IndexItem indexItem : indexItems) {
                try {
                    readKeySet(Long.toString(indexItem.m_uid));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readKeySet(String uid) {

        ObjectIO reader = new ObjectIO(getActivity(), uid + ITEM_LIST);
        ObjectIO mapReader = new ObjectIO(getActivity(), uid);
        Long[] arraySets;

        HashSet<Long> sets = null;
        try {
            sets = (HashSet<Long>) reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TreeMap<Long, FeedItem> mapFromFile = null;
        try {
            mapFromFile = (TreeMap<Long, FeedItem>) mapReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sets != null) {
            listTitle.clear();
            listData.clear();
            listUrl.clear();
            listContent.clear();
            listTContent.clear();
            listTime.clear();

            arraySets = sets.toArray(new Long[sets.size()]);
            Arrays.sort(arraySets);
            for (int i = arraySets.length - 1; i >= 0; i--) {
                FeedItem feedItem = mapFromFile.get(arraySets[i]);
                listTitle.add(feedItem.m_webtitle);
                listData.add(feedItem.m_title);
                listUrl.add(feedItem.m_url);
                listContent.add(feedItem.m_content);
                listTContent.add(feedItem.m_tcontent);
                listTime.add(longToString(feedItem.m_time, "MM-dd HH:mm"));
            }
            listAdapter.refreshData(listTitle, listData, listTContent, listTime);
            mSwipeLayout.setRefreshing(false);
        }
    }
}

