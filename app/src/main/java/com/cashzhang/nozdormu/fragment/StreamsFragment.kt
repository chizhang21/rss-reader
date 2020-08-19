package com.cashzhang.nozdormu.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.cashzhang.nozdormu.MainActivity
import com.cashzhang.nozdormu.MainActivity.FragmentSwitch
import com.cashzhang.nozdormu.R
import com.cashzhang.nozdormu.adapter.FragmentAdapter
import com.cashzhang.nozdormu.adapter.LListAdapter
import com.cashzhang.nozdormu.adapter.StreamAdapter
import com.cashzhang.nozdormu.model.Item
import com.cashzhang.nozdormu.model.MarkAsRead
import com.cashzhang.nozdormu.model.Streams
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Throws

//import com.cashzhang.nozdormu.DialogEditFeed;
/**
 * Created by cz21 on 02/02/2018.
 */
class StreamsFragment : Fragment(), OnRefreshListener {
    private var streamId: ArrayList<String>? = null
    private val streamWebtitle //web title
            : String? = null
    private var streamTitle //title
            : ArrayList<String>? = null
    private var streamUrl //url
            : ArrayList<String>? = null
    private var streamContent //content should display in content fragment
            : ArrayList<String>? = null
    private var streamSummary //content on the list
            : ArrayList<String>? = null
    private var streamTime //publish time
            : ArrayList<String>? = null
    private var entryIDs: ArrayList<String>? = null
    private var streamItems: ArrayList<Item>? = null
    var markAsRead = MarkAsRead()
    private val listAdapter: LListAdapter? = null
    private var bundle: Bundle? = null
    private var activity: Activity? = null
    private var feedId: String? = null
    private var mAdapter: StreamAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    @BindView(R.id.swipe_refresh)
    var mSwipeLayout: SwipeRefreshLayout? = null

    @BindView(R.id.recycler)
    var recyclerView: RecyclerView? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "onCreateView: ")
        val layout = inflater.inflate(R.layout.stream_list, container, false)
        ButterKnife.bind(this, layout)

//        listAdapter = new LListAdapter(activity);
//        recyclerView.setAdapter(listAdapter);
//        recyclerView.setOnItemClickListener(itemClickListener);
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        mAdapter = StreamAdapter(streamItems!!)
        mAdapter!!.setOnItemClickListener(itemClickListener)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter
        mSwipeLayout!!.setOnRefreshListener(this)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        Constants.getFragmentView(this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        streamItems = ArrayList()
        streamId = ArrayList()
        //        streamWebtitle = new ArrayList<>();
        streamTitle = ArrayList()
        streamUrl = ArrayList()
        streamContent = ArrayList()
        streamSummary = ArrayList()
        streamTime = ArrayList()
        entryIDs = ArrayList()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG, "isVisibleToUser: $isVisibleToUser")
        if (isVisibleToUser) {
            try {
                loadData(MainActivity.bundle)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
            //            onRefresh();
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun loadData(tmpBundle: Bundle?) {
        try {
            bundle = tmpBundle
            Log.d(TAG, "loadData: bundle == null? " + (bundle == null))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (bundle != null) {
            val tmpFeedId = bundle!!.getString("feed_id")
            if (tmpFeedId != null && tmpFeedId != "") feedId = tmpFeedId
            if (feedId != null) {
                Log.d(TAG, "loadData: $feedId")
                //            readFromFile(label+".cif");
//                getFeedStream(feedId);
                Thread {
                    try {
                        readFeedStream(feedId!!)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                }.start()

//            } else {
//                Log.d(TAG, "loadData: feedId == null, ready read from file");
//                readFeedStreamFile();
            }
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readFeedStream(feedId: String) {
        streamItems!!.clear()
        val file = File(activity!!.getExternalFilesDir("streams").toString() + "/" + Base64.encodeToString(feedId.toByteArray(charset("UTF-8")), Base64.DEFAULT))
        Log.d(TAG, "readFeedStream: filePath=" + file.absolutePath)
        Log.d(TAG, "readFeedStream: $feedId")
        val `in`: ObjectInput = ObjectInputStream(FileInputStream(file))
        val (_, _, _, _, _, _, items) = `in`.readObject() as Streams
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
            Log.d(TAG, "streamId: "+item.getOriginId());*/streamItems!!.addAll(items)
        activity!!.runOnUiThread { mAdapter!!.refreshData(streamItems!!) }
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

        */
    /*RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
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
        mQueue.add(stringRequest);*/
    /*
    }*/
    override fun onRefresh() {
        Log.d(TAG, "onRefresh: main fragment")
        try {
            loadData(MainActivity.bundle)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        //        Intent intent = new Intent(getActivity(), UpdateService.class);
//        getActivity().startService(intent);
    }

    var itemClickListener: StreamAdapter.ClickListener = object : StreamAdapter.ClickListener {
        override fun onItemClick(position: Int, v: View?) {
            Log.d(TAG, "onItemClick position: $position")
            goContentFragment(position)
        }

        override fun onItemLongClick(position: Int, v: View?) {
            Log.d(TAG, "onItemLongClick pos = $position")
        }
    }

    private fun goContentFragment(position: Int) {
        Log.d(TAG, "goContentFragment: ")
        val contentFragment = ContentFragment()
        val bundle = Bundle()
        //        bundle.putString("id", getId(position));
//        bundle.putString("title", getTitle(position));
//        bundle.putString("time", getTime(position));
//        bundle.putString("url", getUrl(position));
//        bundle.putString("content", getContent(position));
        bundle.putSerializable("item_id", streamItems!![position])
        contentFragment.arguments = bundle
        val mainActivity = getActivity() as MainActivity?
        mainActivity!!.setFragmentSwitch(object : FragmentSwitch {
            override fun gotoFragment(viewPager: ViewPager?, adapter: FragmentAdapter?) {
                mainActivity.setBundle(bundle)
                viewPager!!.currentItem = 4
            }
        })
        mainActivity.forSkip()
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
    private fun getId(position: Int): String? {
        return if (streamItems == null) null else streamItems!![position].id
    }

    private fun getTitle(position: Int): String? {
        return if (streamTitle == null) null else streamTitle!![position]
    }

    private fun getTime(position: Int): String? {
        return if (streamTime == null) null else streamTime!![position]
    }

    private fun getUrl(position: Int): String? {
        return if (streamUrl == null) null else streamUrl!![position]
    }

    private fun getContent(position: Int): String? {
        return if (streamContent == null) null else streamContent!![position]
    }

    /* String Data Long */ /*public void readFromFile() throws IOException, ClassNotFoundException {

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
    @Throws(IOException::class)
    private fun readFeedStreamFile() {
        val file = File("data/data/com.cashzhang.nozdormu/files/FeedStream")
        if (file.exists()) {
            val `is` = FileInputStream(file)
            val b = ByteArray(`is`.available())
            `is`.read(b)
            val response = String(b)
            if (response != "") {
//                parseFeedStream(response);
            }
        }
    } //    private void parseFeedStream(String response) {

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
    companion object {
        private val TAG = StreamsFragment::class.java.simpleName
        fun newInstance(): StreamsFragment {
            return StreamsFragment()
        }

        /* String Data Long */
        @Throws(android.net.ParseException::class)
        fun longToString(currentTime: Long, formatType: String?): String {
            val date = longToDate(currentTime, formatType)
            return dateToString(date, formatType)
        }

        @Throws(android.net.ParseException::class)
        fun longToDate(currentTime: Long, formatType: String?): Date? {
            val dateOld = Date(currentTime)
            val sDateTime = dateToString(dateOld, formatType)
            return stringToDate(sDateTime, formatType)
        }

        fun dateToString(data: Date?, formatType: String?): String {
            return SimpleDateFormat(formatType).format(data)
        }

        @Throws(android.net.ParseException::class)
        fun stringToDate(strTime: String?, formatType: String?): Date? {
            val formatter = SimpleDateFormat(formatType)
            var date: Date? = null
            try {
                date = formatter.parse(strTime)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date
        }
    }
}