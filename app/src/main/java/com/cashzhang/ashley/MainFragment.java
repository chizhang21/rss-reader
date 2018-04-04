package com.cashzhang.ashley;

import android.app.Activity;
import android.app.Dialog;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "ashley-rss";

    private ArrayList<String> listTitle;
    private ArrayList<String> listData;
    private ArrayList<String> listUrl;
    private ArrayList<String> listContent;
    private ArrayList<String> listTContent;
    private ArrayList<String> listTime;

    LListAdapter listAdapter = null;
    ListView listView = null;
    ContentFragment contentFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SwipeRefreshLayout mSwipeLayout;

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

        mSwipeLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_refresh);
        mSwipeLayout.setOnRefreshListener(this);

        listView = (ListView) layout.findViewById(R.id.l_list);
        listAdapter = new LListAdapter(getActivity());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(itemClickListener);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Constants.getFragmentView();
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
                Log.d(TAG, "add feed.");
                return true;
            case android.R.id.home:
                //TODO
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
            // 1. call system browser
            /*Uri uri = Uri.parse(getUrl(position));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);*/
            // 2. use WebView
            /*Intent intent = new Intent(getActivity(), DetailPage.class);
            intent.putExtra("url", getUrl(position));
            startActivity(intent);*/
            // 3. show content
            goContentFragment(position);

        }
    };

    private void goContentFragment(int position) {
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

        HashSet<Long> set = null;
        try {
            set = (HashSet<Long>) reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TreeMap<Long, FeedItem> mapFromFile = null;
        try {
            mapFromFile = (TreeMap<Long, FeedItem>) mapReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (set != null) {
            listTitle.clear();
            listData.clear();
            listUrl.clear();
            listContent.clear();
            listTContent.clear();
            listTime.clear();

            for (Object obj : set) {
                listTitle.add(mapFromFile.get(obj).m_webtitle.toString());
                listData.add(mapFromFile.get(obj).m_title.toString());
                listUrl.add(mapFromFile.get(obj).m_url.toString());
                listContent.add(mapFromFile.get(obj).m_content.toString());
                listTContent.add(mapFromFile.get(obj).m_tcontent.toString());
                listTime.add(longToString(mapFromFile.get(obj).m_time, "HH:mm aa"));

            }
            listAdapter.refreshData(listTitle, listData, listTContent, listTime);
            mSwipeLayout.setRefreshing(false);
        }
    }
}

