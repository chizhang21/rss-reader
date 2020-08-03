package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.cashzhang.nozdormu.DialogEditFeed;
import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.adapter.FragmentAdapter;
import com.cashzhang.nozdormu.adapter.StreamAdapter;
import com.cashzhang.nozdormu.adapter.LListAdapter;
import com.cashzhang.nozdormu.model.Item;
import com.cashzhang.nozdormu.model.MarkAsRead;
import com.cashzhang.nozdormu.model.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by cz21 on 02/02/2018.
 */

public class StreamsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = StreamsFragment.class.getSimpleName();

    private ArrayList<String> streamId;
    private String streamWebtitle;//web title
    private ArrayList<String> streamTitle;//title
    private ArrayList<String> streamUrl;//url
    private ArrayList<String> streamContent;//content should display in content fragment
    private ArrayList<String> streamSummary;//content on the list
    private ArrayList<String> streamTime;//publish time
    
    private ArrayList<String> entryIDs;
    private ArrayList<Item> streamItems;
    
    MarkAsRead markAsRead = new MarkAsRead();
    private LListAdapter listAdapter = null;
    private Bundle bundle;
    private Activity activity;
    private String feedId;

    private StreamAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    public static StreamsFragment newInstance() {
        return new StreamsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView: ");
        View layout = inflater.inflate(R.layout.stream_list, container, false);
        ButterKnife.bind(this, layout);

//        listAdapter = new LListAdapter(activity);
//        recyclerView.setAdapter(listAdapter);
//        recyclerView.setOnItemClickListener(itemClickListener);


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity);
        mAdapter = new StreamAdapter(streamItems);
        mAdapter.setOnItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mSwipeLayout.setOnRefreshListener(this);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Constants.getFragmentView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        streamItems = new ArrayList<>();

        streamId = new ArrayList<>();
//        streamWebtitle = new ArrayList<>();
        streamTitle = new ArrayList<>();
        streamUrl = new ArrayList<>();
        streamContent = new ArrayList<>();
        streamSummary = new ArrayList<>();
        streamTime = new ArrayList<>();
        entryIDs = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "isVisibleToUser: " + isVisibleToUser);
        if (isVisibleToUser) {
            try {
                loadData(MainActivity.bundle);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            onRefresh();
        }
    }

    public void loadData(Bundle tmpBundle) throws IOException, ClassNotFoundException {
        try {
            bundle = tmpBundle;
            Log.d(TAG, "loadData: bundle == null? " + (bundle == null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            String tmpFeedId = bundle.getString("feed_id");
            if (tmpFeedId != null && !tmpFeedId.equals("")) feedId = tmpFeedId;
            if (feedId != null) {
                Log.d(TAG, "loadData: " + feedId);
//            readFromFile(label+".cif");
//                getFeedStream(feedId);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            readFeedStream(feedId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

//            } else {
//                Log.d(TAG, "loadData: feedId == null, ready read from file");
//                readFeedStreamFile();
            }
        }
    }

    private void readFeedStream(final String feedId) throws IOException, ClassNotFoundException {
        streamItems.clear();
        File file = new File(activity.getExternalFilesDir("streams")+"/"+ Base64.encodeToString(feedId.getBytes("UTF-8"), Base64.DEFAULT));
        Log.d(TAG, "readFeedStream: filePath="+file.getAbsolutePath());
        Log.d(TAG, "readFeedStream: "+feedId);
        ObjectInput in = new ObjectInputStream(new FileInputStream(file));
        Streams stream = (Streams) in.readObject();
        /*streamId.add(item.getId());
            Log.d(TAG, "streamId: "+item.getId());
            streamTitle.add(item.getTitle());
            Log.d(TAG, "streamTitle: "+item.getTitle());
            streamSummary.add(item.getSummary().getContent());
            Log.d(TAG, "streamSummaryContent: "+item.getSummary().getContent());
            if (item.getContent() != null)
                streamContent.add(item.getContent().getContent());
            streamTime.add(longToString(item.getPublished(), "MM-dd HH:mm"));
            Log.d(TAG, "streamTime: "+longToString(item.getPublished(), "MM-dd HH:mm"));
            streamUrl.add(item.getOriginId());
            Log.d(TAG, "streamId: "+item.getOriginId());*/
        streamItems.addAll(stream.items);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.refreshData(streamItems);
            }
        });

    }
    
    /*private void getFeedStream(final String feedId) {
        FeedlyApi feedlyApi = FeedlyRequest.getInstance();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feedly-Access-Token", Settings.getAccessToken());
        Log.d(TAG, "accessToken: "+ Settings.getAccessToken());
        CustomListener<Streams> listener = new CustomListener<Streams>() {
            @Override
            public void onNext(Streams response) {
                Log.d(TAG, "onNext");
                streamWebtitle = response.getTitle();
                streamItems = (ArrayList<Item>) response.getItems();
//                for (Item item : response.getItems()) {
//                    collectionLabelList.add(collection.getLabel());
//                    collectionIdList.add(collection.getId());
//                    streamItems.add(item);
//                    streamId.add(item.getId());
//                    streamTitle.add(item.getTitle());
//                    streamSummary.add(item.getSummary().getContent());
//                    streamContent.add(item.getContent().getContent());
//                    streamTime.add(longToString(item.getPublished(),"MM-dd HH:mm"));
//                    streamUrl.add(item.getOriginId());
//                }
//                readCollections();

                mAdapter.refreshData(streamItems);
            }

            @Override
            public void onComplete() {

            }
        };
        RxUtils.CustomSubscribe(feedlyApi.getStreams(feedId,headers), new CustomObserver(listener));

        *//*RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
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
        mQueue.add(stringRequest);*//*
    }*/

    public void onRefresh() {
        Log.d(TAG, "onRefresh: main fragment");
        try {
            loadData(MainActivity.bundle);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent(getActivity(), UpdateService.class);
//        getActivity().startService(intent);
    }

    StreamAdapter.ClickListener itemClickListener =  new StreamAdapter.ClickListener() {

        @Override
        public void onItemClick(int position, View v) {
            Log.d(TAG, "onItemClick position: " + position);
            goContentFragment(position);
        }

        @Override
        public void onItemLongClick(int position, View v) {
            Log.d(TAG, "onItemLongClick pos = " + position);
        }
    };

    private void goContentFragment(int position) {
        Log.d(TAG, "goContentFragment: ");

        ContentFragment contentFragment = new ContentFragment();
        final Bundle bundle = new Bundle();
//        bundle.putString("id", getId(position));
//        bundle.putString("title", getTitle(position));
//        bundle.putString("time", getTime(position));
//        bundle.putString("url", getUrl(position));
//        bundle.putString("content", getContent(position));
        bundle.putSerializable("item_id", streamItems.get(position));
        contentFragment.setArguments(bundle);

        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragmentSwitch(new MainActivity.FragmentSwitch() {
            @Override
            public void gotoFragment(ViewPager viewPager, FragmentAdapter adapter) {
                mainActivity.setBundle(bundle);
                viewPager.setCurrentItem(4);
            }
        });
        mainActivity.forSkip();
//        markAsRead(getId(position));
    }

//    private void markAsRead(final String id) throws JSONException {
//        Log.d(TAG, "markAsRead: id="+id);
//
//        entryIDs.add(id);
//        markAsRead.setAction("markAsRead");
//        markAsRead.setType("entries");
//        markAsRead.setEntryIds(entryIDs);
//
//        final String jsonString = JSONObject.toJSONString(markAsRead);
//
//        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
//
//        //success listener
//        Response.Listener listener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "markAsRead onResponse: " + response);
//            }
//        };
//        //error listener
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.getMessage(), error);
//            }
//        };
//        //POST request
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"/v3/markers",
//                listener, errorListener) {
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                String str = jsonString;
//                return str.getBytes();
//            };
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("X-Feedly-Access-Token", Settings.getAccessToken());
//                return headers;
//            }
//        };
//        VolleyController.getInstance(s_activity).addToRequestQueue(stringRequest);
//    }

    private String getId(int position) {
        return ((streamItems == null) ? null : streamItems.get(position).id);
    }

    private String getTitle(int position) {
        return ((streamTitle == null) ? null : streamTitle.get(position));
    }

    private String getTime(int position) {
        return ((streamTime == null) ? null : streamTime.get(position));
    }

    private String getUrl(int position) {
        return ((streamUrl == null) ? null : streamUrl.get(position));
    }

    private String getContent(int position) {
        return ((streamContent == null) ? null : streamContent.get(position));
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
            streamWebtitle.clear();
            streamTitle.clear();
            streamUrl.clear();
            streamContent.clear();
            streamSummary.clear();
            streamTime.clear();

            arraySets = sets.toArray(new Long[sets.size()]);
            Arrays.sort(arraySets);
            for (int i = arraySets.length - 1; i >= 0; i--) {
                FeedItem feedItem = mapFromFile.get(arraySets[i]);
                streamWebtitle.add(feedItem.m_webtitle);
                streamTitle.add(feedItem.m_title);
                streamUrl.add(feedItem.m_url);
                streamContent.add(feedItem.m_content);
                streamSummary.add(feedItem.m_tcontent);
                streamTime.add(longToString(feedItem.m_time, "MM-dd HH:mm"));
            }
            listAdapter.refreshData(streamWebtitle, streamTitle, streamSummary, streamTime);
            mSwipeLayout.setRefreshing(false);
        }
    }*/



    private void readFeedStreamFile() throws IOException {
        File file = new File("data/data/com.cashzhang.nozdormu/files/FeedStream");
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            String response = new String(b);
            if (!response.equals("")) {
//                parseFeedStream(response);
            }
        }
    }


//    private void parseFeedStream(String response) {
//        FeedStream feedStream = JSON.parseObject(response, FeedStream.class);
//        streamItems = (ArrayList<FeedStreamItems>) feedStream.getItems();
//        //TODO clear
//        if (streamItems != null) {
//            streamId.clear();
//            streamWebtitle.clear();
//            streamTitle.clear();
//            streamContent.clear();
//            streamSummary.clear();
//            streamUrl.clear();
//            streamTime.clear();
//        }
//
//        for (FeedStreamItems listItem: streamItems) {
//            //streamWebtitle, streamTitle, streamUrl , streamContent, streamSummary, streamTime
//            streamId.add(listItem.getId());
//            streamWebtitle.add(feedStream.getTitle());
//            streamTitle.add(listItem.getTitle());
//            if (listItem.getContent() != null)
//                streamContent.add(listItem.getContent().getContent());
//            else if (listItem.getSummary() != null)
//                streamContent.add(listItem.getSummary().getContent());
//            streamSummary.add(UpdateService.Patterns.CDATA.matcher(listItem.getSummary().getContent()).replaceAll("").trim());
////            streamUrl.add(listItem.getAlternate().get(0).getHref());
//            streamUrl.add(listItem.getOriginId());
//            streamTime.add(longToString(listItem.getPublished(),"MM-dd HH:mm"));
//        }
//        listAdapter.refreshData(streamWebtitle, streamTitle, streamSummary, streamTime);
//        mSwipeLayout.setRefreshing(false);
//    }

}

