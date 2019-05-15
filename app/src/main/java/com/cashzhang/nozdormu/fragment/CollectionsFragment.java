package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cashzhang.nozdormu.FeedlyApi;
import com.cashzhang.nozdormu.FeedlyRequest;
import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.ObjectIO;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.RxUtils;
import com.cashzhang.nozdormu.Settings;
import com.cashzhang.nozdormu.adapter.CollectionsListAdapter;
import com.cashzhang.nozdormu.adapter.FrogAdapter;
import com.cashzhang.nozdormu.bean.Collection;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CollectionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = CollectionsFragment.class.getSimpleName();

    private ArrayList<String> collectionLabelList;
    private ArrayList<String> collectionIdList;

    CollectionsListAdapter collectionsAdapter = null;
    FeedsFragment feedsFragment;
    Activity activity;

    ObjectIO objectIO;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.l_list)
    ListView listView;

    public static CollectionsFragment newInstance() {
        CollectionsFragment collectionsFragment = new CollectionsFragment();
        return collectionsFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
            objectIO = new ObjectIO(activity);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView: categ fragment");
        View layout = inflater.inflate(R.layout.feed_list, container, false);
        ButterKnife.bind(this, layout);

        mSwipeLayout.setOnRefreshListener(this);
        collectionsAdapter = new CollectionsListAdapter(activity);
        listView.setAdapter(collectionsAdapter);
        listView.setOnItemClickListener(itemClickListener);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        collectionLabelList = new ArrayList<>();
        collectionIdList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "isVisibleToUser: " + isVisibleToUser);
        if (isVisibleToUser) {
//            ((AppCompatActivity)activity).getSupportActionBar().show();
            mSwipeLayout.setRefreshing(true);
            getCollections();
        }
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: CollectionsFragment");
        getCollections();
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goContentFragment(position);
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
            public void gotoFragment(ViewPager viewPager, FrogAdapter adapter) {
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
        for (String string :
                collectionLabelList) {
            Log.d(TAG, "readCollections: "+string);
        }

        collectionsAdapter.refreshData(collectionLabelList);
        mSwipeLayout.setRefreshing(false);
    }

    public void getCollections() {

        FeedlyApi feedlyApi = FeedlyRequest.getInstance();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feedly-Access-Token", Settings.getAccessToken());


        /*OnNextListener<List<Collection>> listener = new OnNextListener<List<Collection>>() {
            @Override
            public void onNext(List<Collection> collections) throws IOException, ClassNotFoundException {
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
//                readCollections();
            }
        };*/

        Observer<List<Collection>> observer = new Observer<List<Collection>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: Current Thread="+Thread.currentThread().getName());
            }

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
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: Current Thread="+Thread.currentThread().getName());
                readCollections();
            }
        };

        RxUtils.CustomSubscribe(feedlyApi.getCollections(headers), observer);
    }

    private boolean writeEachCollectionToFile(Collection collection) throws FileNotFoundException {
        if (collection == null)
            return false;
        objectIO.setNewFileName(collection.getLabel());
        return objectIO.write(collection);
    }


}

