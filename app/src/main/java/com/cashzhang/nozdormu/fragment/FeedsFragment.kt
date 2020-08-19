package com.cashzhang.nozdormu.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
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
import com.cashzhang.nozdormu.adapter.FeedAdapter
import com.cashzhang.nozdormu.adapter.FragmentAdapter
import com.cashzhang.nozdormu.model.Feed
import java.io.*
import java.util.*
import kotlin.Throws

class FeedsFragment : Fragment(), OnRefreshListener {
    private var feedTitleList: ArrayList<String>? = null
    private var feedIdList: ArrayList<String>? = null
    private var bundle: Bundle? = null
    private var activity: Activity? = null
    private var mAdapter: FeedAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    @BindView(R.id.swipe_refresh)
    var mSwipeLayout: SwipeRefreshLayout? = null

    @BindView(R.id.recycler)
    var recyclerView: RecyclerView? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        if (context is Activity) {
            activity = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "onCreateView")
        val layout = inflater.inflate(R.layout.feed_list, container, false)
        ButterKnife.bind(this, layout)
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        mAdapter = FeedAdapter(feedTitleList!!)
        mAdapter!!.setOnItemClickListener(itemClickListener)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter
        mSwipeLayout!!.setOnRefreshListener(this)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setHasOptionsMenu(true)
        feedIdList = ArrayList()
        feedTitleList = ArrayList()
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
            /*mSwipeLayout.setRefreshing(true);
            onRefresh();*/
            try {
                loadData(MainActivity.bundle)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun onRefresh() {
        try {
            mSwipeLayout!!.isRefreshing = true
            loadData(MainActivity.bundle)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    var itemClickListener: FeedAdapter.ClickListener = object : FeedAdapter.ClickListener {
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
        val streamsFragment = StreamsFragment()
        val bundle = Bundle()
        bundle.putString("feed_id", getFeedId(position))
        streamsFragment.arguments = bundle
        val mainActivity = activity as MainActivity?
        mainActivity!!.setFragmentSwitch(object : FragmentSwitch {
            override fun gotoFragment(viewPager: ViewPager?, adapter: FragmentAdapter?) {
                mainActivity.setBundle(bundle)
                viewPager!!.currentItem = 3
            }
        })
        mainActivity.forSkip()
    }

    private fun getFeedId(position: Int): String {
        return feedIdList!![position]
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun loadData(tmpBundle: Bundle?) {
        try {
            bundle = tmpBundle
            Log.d(TAG, "loadData: bundle == null? " + (bundle == null))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (bundle != null) {
            val label = bundle!!.getString("categ_lebel")
            Log.d(TAG, "bundle != null, label == $label")
            if (label != null) {
                feedTitleList!!.clear()
                feedIdList!!.clear()
                //begin reading data, set refreshing
//                mSwipeLayout.setRefreshing(true);
                /*ObjectIO objectIO = new ObjectIO(activity, label, 1);
                Collection collection = (Collection) objectIO.read();*/
                val files = File(activity!!.getExternalFilesDir("collections/$label").toString() + "/").listFiles()
                for (file in files) {
                    val `in`: ObjectInput = ObjectInputStream(FileInputStream(file))
                    val (feedId, _, title) = `in`.readObject() as Feed
                    feedTitleList!!.add(title)
                    feedIdList!!.add(feedId)
                }

                /*for (Feed feed: collection.getFeeds()) {
                    Log.d(TAG, "loadData: feedLabel="+feed.getTitle());
                    feedTitleList.add(feed.getTitle());
                    feedIdList.add(feed.getFeedId());
                }*/mAdapter!!.refreshData(feedTitleList!!)
                mSwipeLayout!!.isRefreshing = false
            }
        } else if (!feedTitleList!!.isEmpty()) { //TODO memory cache
            mAdapter!!.refreshData(feedTitleList!!)
        }
    } /*public void readFromFile(String fileLabelName) throws IOException, ClassNotFoundException {
        String response;

        File file = new File("data/data/com.cashzhang.nozdormu/files/" + fileLabelName);
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            response = new String(b);
            if (!response.equals("")) {

                if (feedTitleList != null)
                    feedTitleList.clear();
                if (feedIdList != null)
                    feedIdList.clear();


                */

    /*JSONArray jsonArray = JSONArray.parseArray(response);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    feedIdList.add(jsonObject.get("id").toString());
                    feedTitleList.add(jsonObject.get("title").toString());
                }*/
    /*
                listAdapter.refreshData(feedTitleList);
            }
        }
        mSwipeLayout.setRefreshing(false);
    }*/
    companion object {
        private val TAG = FeedsFragment::class.java.simpleName
        fun newInstance(): FeedsFragment {
            return FeedsFragment()
        }
    }
}