package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cashzhang.nozdormu.CustomObserver;
import com.cashzhang.nozdormu.FeedlyApi;
import com.cashzhang.nozdormu.FeedlyRequest;
import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.ObjectIO;
import com.cashzhang.nozdormu.CustomListener;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.RxUtils;
import com.cashzhang.nozdormu.Settings;
import com.cashzhang.nozdormu.adapter.FragmentAdapter;
import com.cashzhang.nozdormu.adapter.CollectionAdapter;
import com.cashzhang.nozdormu.bean.Collection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = CollectionsFragment.class.getSimpleName();

    private ArrayList<String> collectionLabelList;
    private ArrayList<String> collectionIdList;

    private FeedsFragment feedsFragment;
    private Activity activity;
    private ObjectIO objectIO;
    
    private CollectionAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof Activity) {
            activity = (Activity) context;
            objectIO = new ObjectIO(activity,1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView");
        View layout = inflater.inflate(R.layout.collection_list, container, false);
        ButterKnife.bind(this, layout);
        
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity);
        mAdapter = new CollectionAdapter(collectionLabelList);
        mAdapter.setOnItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(layoutManager);

        mSwipeLayout.setOnRefreshListener(this);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
        collectionLabelList = new ArrayList<>();
        collectionIdList = new ArrayList<>();
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
            recyclerView.setAdapter(mAdapter);
//            getCollections();
            readCollections();
        }
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh");
//        getCollections();
        readCollections();
    }

    /*AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goContentFragment(position);
        }
    };*/

   CollectionAdapter.ClickListener itemClickListener =  new CollectionAdapter.ClickListener() {

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

        feedsFragment = new FeedsFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("categ_lebel", getLabel(position));
        feedsFragment.setArguments(bundle);

        final MainActivity mainActivity = (MainActivity) activity;
        mainActivity.setFragmentSwitch(new MainActivity.FragmentSwitch() {
            @Override
            public void gotoFragment(ViewPager viewPager, FragmentAdapter adapter) {
                mainActivity.setBundle(bundle);
                viewPager.setCurrentItem(2);
            }
        });
        mainActivity.forSkip();

    }

    private String getLabel(int position) {
        return ((collectionLabelList == null) ? null : collectionLabelList.get(position));
    }

    private String getCollectionId(int position) {
        return ((collectionIdList == null) ? null : collectionIdList.get(position));
    }

    public void readCollections() {
        /*for (String label: collectionLabelList) {
            objectIO.setNewFileName(label);
            objectIO.read();
        }*/
        collectionLabelList.clear();
        File[] files = new File(activity.getExternalFilesDir("collections")+"/").listFiles();
        for (File file : files) {
            collectionLabelList.add(file.getName());
            Log.d(TAG, "readCollections: "+file.getName());
        }

        mAdapter.refreshData(collectionLabelList);
        mSwipeLayout.setRefreshing(false);
    }

    /*public void getCollections() {

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


}

