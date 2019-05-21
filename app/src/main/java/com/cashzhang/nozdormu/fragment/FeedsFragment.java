package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.ObjectIO;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.adapter.FeedAdapter;
import com.cashzhang.nozdormu.adapter.FragmentAdapter;
import com.cashzhang.nozdormu.bean.Collection;
import com.cashzhang.nozdormu.bean.Feed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = FeedsFragment.class.getSimpleName();

    private ArrayList<String> feedTitleList;
    private ArrayList<String> feedIdList;

    private Bundle bundle;
    private Activity activity;

    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView");
        View layout = inflater.inflate(R.layout.feed_list, container, false);
        ButterKnife.bind(this, layout);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity);
        mAdapter = new FeedAdapter(feedTitleList);
        mAdapter.setOnItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        
        mSwipeLayout.setOnRefreshListener(this);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
        feedIdList = new ArrayList<>();
        feedTitleList = new ArrayList<>();
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
            /*mSwipeLayout.setRefreshing(true);
            onRefresh();*/
            try {
                loadData(MainActivity.getBundle());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    public void onRefresh() {
        try {
            mSwipeLayout.setRefreshing(true);
            loadData(MainActivity.getBundle());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    FeedAdapter.ClickListener itemClickListener =  new FeedAdapter.ClickListener() {

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

        StreamsFragment streamsFragment = new StreamsFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("feed_id", getFeedId(position));
        streamsFragment.setArguments(bundle);

        final MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setFragmentSwitch(new MainActivity.FragmentSwitch() {
            @Override
            public void gotoFragment(ViewPager viewPager, FragmentAdapter adapter) {
                mainActivity.setBundle(bundle);
                viewPager.setCurrentItem(3);
            }
        });
        mainActivity.forSkip();

    }
    private String getFeedId(int position) {
        return feedIdList.get(position);
    }

    private void loadData(Bundle tmpBundle) throws IOException, ClassNotFoundException {
        try {
            bundle = tmpBundle;
            Log.d(TAG, "loadData: bundle == null? " + (bundle == null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            String label = bundle.getString("categ_lebel");
            Log.d(TAG, "bundle != null, label == " + label);
            if (label != null) {
                feedTitleList.clear();
                feedIdList.clear();
                //begin reading data, set refreshing
//                mSwipeLayout.setRefreshing(true);
                /*ObjectIO objectIO = new ObjectIO(activity, label, 1);
                Collection collection = (Collection) objectIO.read();*/
                File[] files = new File(activity.getExternalFilesDir("collections/"+label)+"/").listFiles();
                for (File file : files) {
                    ObjectInput in = new ObjectInputStream(new FileInputStream(file));
                    Feed feed = (Feed) in.readObject();
                    feedTitleList.add(feed.getTitle());
                    feedIdList.add(feed.getFeedId());
                }

                /*for (Feed feed: collection.getFeeds()) {
                    Log.d(TAG, "loadData: feedLabel="+feed.getTitle());
                    feedTitleList.add(feed.getTitle());
                    feedIdList.add(feed.getFeedId());
                }*/
                
                mAdapter.refreshData(feedTitleList);
                mSwipeLayout.setRefreshing(false);
            }
        } else if (!feedTitleList.isEmpty()) {//TODO memory cache
            mAdapter.refreshData(feedTitleList);
        }
    }



    /*public void readFromFile(String fileLabelName) throws IOException, ClassNotFoundException {
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


                *//*JSONArray jsonArray = JSONArray.parseArray(response);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    feedIdList.add(jsonObject.get("id").toString());
                    feedTitleList.add(jsonObject.get("title").toString());
                }*//*
                listAdapter.refreshData(feedTitleList);
            }
        }
        mSwipeLayout.setRefreshing(false);
    }*/

}

