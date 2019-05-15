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
import com.cashzhang.nozdormu.Constants;
import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.ObjectIO;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.adapter.CollectionsListAdapter;
import com.cashzhang.nozdormu.adapter.FrogAdapter;
import com.cashzhang.nozdormu.bean.Collection;
import com.cashzhang.nozdormu.bean.Feed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = FeedsFragment.class.getSimpleName();

    private ArrayList<String> listTitle;
    private ArrayList<String> listFeedId;
    private ArrayList<Feed> feedsList;
    private Bundle bundle;
    private CollectionsListAdapter listAdapter = null;
    private MainFragment mainFragment;
    private Activity activity;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.l_list)
    ListView listView;

    private String label;

    public static FeedsFragment newInstance() {
        FeedsFragment feedsFragment = new FeedsFragment();
        return feedsFragment;
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
        View layout = inflater.inflate(R.layout.feed_list, container, false);
        ButterKnife.bind(this, layout);

        mSwipeLayout.setOnRefreshListener(this);
        listAdapter = new CollectionsListAdapter(activity);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(itemClickListener);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Constants.getSecCatesFragmentView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listFeedId = new ArrayList<String>();
        listTitle = new ArrayList<String>();
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

    /*@Override
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
    }*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "isVisibleToUser: " + isVisibleToUser);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onRefresh() {
        try {
            readFromFile(label+".cif");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goContentFragment(position);
        }
    };

    private void goContentFragment(int position) {
        Log.d(TAG, "goContentFragment: ");

        mainFragment = new MainFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("feed_id", getFeedId(position));
        mainFragment.setArguments(bundle);

        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragmentSwitch(new MainActivity.FragmentSwitch() {
            @Override
            public void gotoFragment(ViewPager viewPager, FrogAdapter adapter) {
                mainActivity.setBundle(bundle);
                viewPager.setCurrentItem(3);
            }
        });
        mainActivity.forSkip();

    }
    private String getFeedId(int position) {
        return feedsList.get(position).getFeedId();
    }

    public void loadData(Bundle tmpBundle) throws IOException, ClassNotFoundException {
        try {
            bundle = tmpBundle;
            Log.d(TAG, "loadData: bundle == null? " + (bundle == null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            label = bundle.getString("categ_lebel");
            Log.d(TAG, "SecFragm: bundle != null, label == " + label);
            if (label != null) {
                //begin reading data, set refreshing
                mSwipeLayout.setRefreshing(true);
                ObjectIO objectIO = new ObjectIO(activity, label);
                Collection collection = (Collection) objectIO.read();
                feedsList = (ArrayList<Feed>) collection.getFeeds();
                for (Feed feed: feedsList) {
                    Log.d(TAG, "loadData: feedLabel="+feed.getTitle());
                }

            }
        }
    }



    public void readFromFile(String fileLabelName) throws IOException, ClassNotFoundException {
        String response;

        File file = new File("data/data/com.cashzhang.nozdormu/files/" + fileLabelName);
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            response = new String(b);
            if (!response.equals("")) {

                if (listTitle != null)
                    listTitle.clear();
                if (listFeedId != null)
                    listFeedId.clear();


                /*JSONArray jsonArray = JSONArray.parseArray(response);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    listFeedId.add(jsonObject.get("id").toString());
                    listTitle.add(jsonObject.get("title").toString());
                }*/
                listAdapter.refreshData(listTitle);
            }
        }
        mSwipeLayout.setRefreshing(false);
    }

}

