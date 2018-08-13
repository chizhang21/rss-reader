package com.cashzhang.ashley.fragment;

import android.app.Activity;
import android.app.Dialog;
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
import android.text.format.Time;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cashzhang.ashley.AuthView;
import com.cashzhang.ashley.Constants;
import com.cashzhang.ashley.DialogEditFeed;
import com.cashzhang.ashley.FeedItem;
import com.cashzhang.ashley.IndexItem;
import com.cashzhang.ashley.MainActivity;
import com.cashzhang.ashley.ObjectIO;
import com.cashzhang.ashley.R;
import com.cashzhang.ashley.Settings;
import com.cashzhang.ashley.VolleyController;
import com.cashzhang.ashley.adapter.FrogAdapter;
import com.cashzhang.ashley.adapter.LListAdapter;
import com.cashzhang.ashley.bean.Categ;
import com.cashzhang.ashley.bean.CategItem;
import com.cashzhang.ashley.bean.FeedStream;
import com.cashzhang.ashley.bean.FeedStreamItems;
import com.cashzhang.ashley.bean.MarkAsRead;
import com.cashzhang.ashley.bean.Token;
import com.cashzhang.ashley.service.ServiceUpdate;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cashzhang.ashley.Constants.s_activity;
import static com.cashzhang.ashley.service.ServiceUpdate.ITEM_LIST;

/**
 * Created by hadoop on 02/02/2018.
 */

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "ashley-rss";

    private ArrayList<String> listId;
    private ArrayList<String> listTitle;//web title
    private ArrayList<String> listData;//title
    private ArrayList<String> listUrl;//url
    private ArrayList<String> listContent;//content should display in content fragment
    private ArrayList<String> listTContent;//content on the list
    private ArrayList<String> listTime;//publish time

    List<String> entryIDs = new ArrayList<String>();

    private ArrayList<FeedStreamItems> listItems;
    MarkAsRead markAsRead = new MarkAsRead();
    LListAdapter listAdapter = null;
    ContentFragment contentFragment;
    Bundle bundle;
    private String feedId;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.l_list)
    ListView listView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    private final BroadcastReceiver m_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != s_activity) {
                Log.d(TAG, "onReceive: main fragment");
                /*try {
                    readFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }*/
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            activity.registerReceiver(m_broadcastReceiver, new IntentFilter(ServiceUpdate.FEED_BROADCAST_ACTION));
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
        Constants.getFragmentView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listId = new ArrayList<String>();
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
//        inflater.inflate(R.menu.feeds_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_feed:
                MainActivity activity = (MainActivity) getActivity();
                Dialog dialog = DialogEditFeed.newInstance(activity, -1);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        try {
//            readFromFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
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

    public void loadData(Bundle tmpBundle) throws IOException, ClassNotFoundException {
        try {
            bundle = tmpBundle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            String tmpFeedId = bundle.getString("feed_id");
            if (tmpFeedId != null && !tmpFeedId.equals("")) feedId = tmpFeedId;
            if (feedId != null) {
                Log.d(TAG, "MainFragment loadData: " + feedId);
//            readFromFile(label+".cif");
                getFeedStreamById(feedId);
            } else {
                Log.d(TAG, "loadData: feedId == null, ready read from file");
                readFeedStreamFile();
            }
        } else {
            Log.d(TAG, "loadData: bundle == null, ready read from file");
            readFeedStreamFile();
        }

    }

    private void getFeedStreamById(final String feedId) {
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Constants.tmpWrite("FeedStream", response);
                //TODO parse for each feed item
                parseFeedStream(response);
            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        String input = "";
        try {
            input = Constants.BASE_URL + "/v3/streams/contents?streamId=" + URLEncoder.encode(feedId, "UTF-8") + "&unreadOnly=true";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //GET request
        StringRequest stringRequest = new StringRequest(input,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Feedly-Access-Token", Settings.getAccessToken());
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: main fragment");
        try {
            loadData(MainActivity.getBundle());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent(getActivity(), ServiceUpdate.class);
//        getActivity().startService(intent);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                goContentFragment(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void goContentFragment(int position) throws JSONException {
        Log.d(TAG, "goContentFragment: ");

        contentFragment = new ContentFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("id", getId(position));
        bundle.putString("title", getTitle(position));
        bundle.putString("time", getTime(position));
        bundle.putString("url", getUrl(position));
        bundle.putString("content", getContent(position));
        contentFragment.setArguments(bundle);

        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragmentSwitch(new MainActivity.FragmentSwitch() {
            @Override
            public void gotoFragment(ViewPager viewPager, FrogAdapter adapter) {
                mainActivity.setBundle(bundle);
                viewPager.setCurrentItem(4);
            }
        });
        mainActivity.forSkip();
        markAsRead(getId(position));
    }

    private void markAsRead(final String id) throws JSONException {
        Log.d(TAG, "markAsRead: id="+id);

        entryIDs.add(id);
        markAsRead.setAction("markAsRead");
        markAsRead.setType("entries");
        markAsRead.setEntryIds(entryIDs);

        final String jsonString = JSONObject.toJSONString(markAsRead);

        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);

        //success listener
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "markAsRead onResponse: " + response);
            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"/v3/markers",
                listener, errorListener) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                String str = jsonString;
                return str.getBytes();
            };

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", Settings.getAccessToken());
                return headers;
            }
        };
        VolleyController.getInstance(s_activity).addToRequestQueue(stringRequest);
    }

    private String getId(int position) {
        return ((listId == null) ? null : listId.get(position));
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

    /*public void readFromFile() throws IOException, ClassNotFoundException {

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
    }*/



    private void readFeedStreamFile() throws IOException {
        File file = new File("data/data/com.cashzhang.ashley/files/FeedStream");
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            String response = new String(b);
            if (!response.equals("")) {
                parseFeedStream(response);
            }
        }
    }


    private void parseFeedStream(String response) {
        FeedStream feedStream = JSON.parseObject(response, FeedStream.class);
        listItems = (ArrayList<FeedStreamItems>) feedStream.getItems();
        //TODO clear
        if (listItems != null) {
            listId.clear();
            listTitle.clear();
            listData.clear();
            listContent.clear();
            listTContent.clear();
            listUrl.clear();
            listTime.clear();
        }

        for (FeedStreamItems listItem: listItems) {
            //listTitle, listData, listUrl , listContent, listTContent, listTime
            listId.add(listItem.getId());
            listTitle.add(feedStream.getTitle());
            listData.add(listItem.getTitle());
            if (listItem.getContent() != null)
                listContent.add(listItem.getContent().getContent());
            else if (listItem.getSummary() != null)
                listContent.add(listItem.getSummary().getContent());
            listTContent.add(ServiceUpdate.Patterns.CDATA.matcher(listItem.getSummary().getContent()).replaceAll("").trim());
//            listUrl.add(listItem.getAlternate().get(0).getHref());
            listUrl.add(listItem.getOriginId());
            listTime.add(longToString(listItem.getPublished(),"MM-dd HH:mm"));
        }
        listAdapter.refreshData(listTitle, listData, listTContent, listTime);
        mSwipeLayout.setRefreshing(false);
    }

}

