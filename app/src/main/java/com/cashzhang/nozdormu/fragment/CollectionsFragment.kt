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
import com.cashzhang.nozdormu.ObjectIO
import com.cashzhang.nozdormu.R
import com.cashzhang.nozdormu.adapter.CollectionAdapter
import com.cashzhang.nozdormu.adapter.FragmentAdapter
import java.io.File
import java.util.*
import kotlin.Throws

class CollectionsFragment : Fragment(), OnRefreshListener {
    private var collectionLabelList: ArrayList<String>? = null
    private var collectionIdList: ArrayList<String>? = null
    private var feedsFragment: FeedsFragment? = null
    private var activity: Activity? = null
    private var objectIO: ObjectIO? = null
    private var mAdapter: CollectionAdapter? = null
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
            objectIO = ObjectIO(activity!!, 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "onCreateView")
        val layout = inflater.inflate(R.layout.collection_list, container, false)
        ButterKnife.bind(this, layout)
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        mAdapter = CollectionAdapter(collectionLabelList!!)
        mAdapter!!.setOnItemClickListener(itemClickListener)
        recyclerView!!.layoutManager = layoutManager
        mSwipeLayout!!.setOnRefreshListener(this)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setHasOptionsMenu(true)
        collectionLabelList = ArrayList()
        collectionIdList = ArrayList()
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
            recyclerView!!.adapter = mAdapter
            //            getCollections();
            readCollections()
        }
    }

    override fun onRefresh() {
        Log.d(TAG, "onRefresh")
        //        getCollections();
        readCollections()
    }

    /*AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goContentFragment(position);
        }
    };*/
    var itemClickListener: CollectionAdapter.ClickListener = object : CollectionAdapter.ClickListener {
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
        feedsFragment = FeedsFragment()
        val bundle = Bundle()
        bundle.putString("categ_lebel", getLabel(position))
        feedsFragment!!.arguments = bundle
        val mainActivity = activity as MainActivity?
        mainActivity!!.setFragmentSwitch(object : FragmentSwitch {
            override fun gotoFragment(viewPager: ViewPager?, adapter: FragmentAdapter?) {
                mainActivity.setBundle(bundle)
                viewPager!!.currentItem = 2
            }
        })
        mainActivity.forSkip()
    }

    private fun getLabel(position: Int): String? {
        return if (collectionLabelList == null) null else collectionLabelList!![position]
    }

    private fun getCollectionId(position: Int): String? {
        return if (collectionIdList == null) null else collectionIdList!![position]
    }

    fun readCollections() {
        /*for (String label: collectionLabelList) {
            objectIO.setNewFileName(label);
            objectIO.read();
        }*/
        collectionLabelList!!.clear()
        val files = File(activity!!.getExternalFilesDir("collections").toString() + "/").listFiles()
        for (file in files) {
            collectionLabelList!!.add(file.name)
            Log.d(TAG, "readCollections: " + file.name)
        }
        mAdapter!!.refreshData(collectionLabelList!!)
        mSwipeLayout!!.isRefreshing = false
    } /*public void getCollections() {

        FeedlyApi feedlyApi = FeedlyRequest.getInstance();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feedly-Access-Token", Settings.getAccessToken());


        CustomListener<List<Collection>> listener = new CustomListener<List<Collection>>() {
            @Override
            public void onNext(List<Collection> collections) {
                collectionLabelList.clear();
                collectionIdList.clear();
                for (Collection collection : collections) {
                    collectionLabelList.add(collection.getLabel());
                    collectionIdList.add(collection.getId());

                    boolean writeStatus = false;
                    try {
                        writeStatus = writeEachCollectionToFile(collection);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "writeStatus: "+writeStatus);
                }
                readCollections();
            }

            @Override
            public void onComplete() {

            }
        };
        RxUtils.CustomSubscribe(feedlyApi.getCollections(headers), new CustomObserver(listener));
    }

    private boolean writeEachCollectionToFile(Collection collection) throws FileNotFoundException {
        if (collection == null)
            return false;
        objectIO.setNewFileName(collection.getLabel());
        return objectIO.write(collection);
    }*/

    companion object {
        private val TAG = CollectionsFragment::class.java.simpleName
        fun newInstance(): CollectionsFragment {
            return CollectionsFragment()
        }
    }
}