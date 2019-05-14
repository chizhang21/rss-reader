package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cashzhang.nozdormu.Constants;
//import com.cashzhang.nozdormu.DialogEditFeed;
import com.cashzhang.nozdormu.CustomObserver;
import com.cashzhang.nozdormu.FeedlyApi;
import com.cashzhang.nozdormu.FeedlyRequest;
import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.ObjectIO;
import com.cashzhang.nozdormu.OnNextListener;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.RxUtils;
import com.cashzhang.nozdormu.Settings;
import com.cashzhang.nozdormu.adapter.CollectionsListAdapter;
import com.cashzhang.nozdormu.adapter.FrogAdapter;
import com.cashzhang.nozdormu.bean.Collection;
import com.cashzhang.nozdormu.bean.Feed;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;


public class CollectionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = CollectionsFragment.class.getSimpleName();

    private ArrayList<String> listLabel;
    private ArrayList<String> listId;

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
//        Constants.getCatesFragmentView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listLabel = new ArrayList<String>();
        listId = new ArrayList<String>();
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_feed:
//                MainActivity activity = (MainActivity) getActivity();
//                Dialog dialog = DialogEditFeed.newInstance(activity, -1);
//                dialog.show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //readCollections();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint() -> isVisibleToUser: " + isVisibleToUser);
        if (isVisibleToUser) {
//            ((AppCompatActivity)activity).getSupportActionBar().show();
        }
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: categ fragment");
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

        final MainActivity mainActivity = (MainActivity) getActivity();
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
        return ((listLabel == null) ? null : listLabel.get(position));
    }

    private String getCategId(int position) {
        return ((listId == null) ? null : listId.get(position));
    }

    public void readCollections() {
        /*for (String label: listLabel) {
            objectIO.setNewFileName(label);
            objectIO.read();
        }*/
        for (String string :
                listLabel) {
            Log.d(TAG, "readCollections: "+string);
        }

        collectionsAdapter.refreshData(listLabel);
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
                    listLabel.add(collection.getLabel());
                    listId.add(collection.getId());

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
                Log.d(TAG, "onSubscribe: Current Thread="+Thread.currentThread());
            }

            @Override
            public void onNext(List<Collection> collections) {
                for (Collection collection : collections) {
                    listLabel.add(collection.getLabel());
                    listId.add(collection.getId());

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
                Log.d(TAG, "onComplete: Current Thread="+Thread.currentThread());
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

