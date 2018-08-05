package com.cashzhang.ashley.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cashzhang.ashley.Constants;
import com.cashzhang.ashley.DialogEditFeed;
import com.cashzhang.ashley.MainActivity;
import com.cashzhang.ashley.R;
import com.cashzhang.ashley.Settings;
import com.cashzhang.ashley.adapter.CategListAdapter;
import com.cashzhang.ashley.adapter.FrogAdapter;
import com.cashzhang.ashley.bean.Categ;
import com.cashzhang.ashley.bean.CategItem;
import com.cashzhang.ashley.service.SyncCatesListService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cashzhang.ashley.Constants.s_activity;

public class CategsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "ashley-rss";

    private ArrayList<String> listLabel;
    private ArrayList<String> listId;

    CategListAdapter listAdapter = null;
    SecCategsFragment secCategsFragment;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.l_list)
    ListView listView;

    public static CategsFragment newInstance() {
        CategsFragment categsFragment = new CategsFragment();
        return categsFragment;
    }

    private final BroadcastReceiver m_broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != s_activity) {
                Log.d(TAG, "onReceive: categs fragment");
                try {
                    readFromFile();
                    getSubs();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            activity.registerReceiver(m_broadcastReceiver, new IntentFilter(SyncCatesListService.CATEG_BROADCAST_ACTION));
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
        listAdapter = new CategListAdapter(getActivity());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(itemClickListener);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Constants.getCatesFragmentView(this);
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
        inflater.inflate(R.menu.feeds_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_feed:
                MainActivity activity = (MainActivity) getActivity();
                Dialog dialog = DialogEditFeed.newInstance(activity, -1);
                dialog.show();
                Log.d(TAG, "add feed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint() -> isVisibleToUser: " + isVisibleToUser);
        if (isVisibleToUser) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        }
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: categ fragment");
        Intent intent = new Intent(getActivity(), SyncCatesListService.class);
        getActivity().startService(intent);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            goContentFragment(position);
        }
    };

    private void goContentFragment(int position) {
        Log.d(TAG, "goContentFragment: ");

        secCategsFragment = new SecCategsFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("categ_lebel", getLabel(position));
        secCategsFragment.setArguments(bundle);

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

    public void readFromFile() throws IOException, ClassNotFoundException {
        String response;

        File file = new File("data/data/com.cashzhang.ashley/files/categ_list_" + Settings.getId());
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            byte[] b = new byte[is.available()];
            is.read(b);
            response = new String(b);
            if (!response.equals("")) {

                if (listLabel != null)
                    listLabel.clear();
                if (listId != null)
                    listId.clear();

                JSONArray jsonArray = JSONArray.parseArray(response);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    listId.add(jsonObject.get("id").toString());
                    listLabel.add(jsonObject.get("label").toString());
                }
                listAdapter.refreshData(listLabel);
            }
        }
        mSwipeLayout.setRefreshing(false);
    }

    public void getSubs() {
        //TODO delete categ item file
        deleteCategItemFile();
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = JSONArray.parseArray(response);
                for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    CategItem categItem = JSONObject.toJavaObject(jsonObject, CategItem.class);
                    for (Categ categ : categItem.getCategories()) {
                        writeEachCategItemToFile(categ.getLabel() + ".cif", JSON.toJSONString(categItem) + ",");
                    }
                }
                try {
                    modifyEachFile(listLabel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        StringRequest stringRequest = new StringRequest(Constants.BASE_URL + Constants.SUBSCRIPTIONS,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", Settings.getAccessToken());
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

    private void writeEachCategItemToFile(String fileName, String categItemJson) {
        String currentUserDir = "data/data/com.cashzhang.ashley/files/";
        File file = new File(currentUserDir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeBracket(fileName, "[");
                writeEachCategItemToFile(fileName, categItemJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(categItemJson.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeBracket(String fileName, String bracket) {
        String currentUserDir = "data/data/com.cashzhang.ashley/files/";
        File file = new File(currentUserDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bracket.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifyEachFile(ArrayList<String> labels) throws IOException {
        for (String label : labels) {
            modifyFileContent(label+".cif");
        }
    }

    private void modifyFileContent(String lebel) throws IOException {
        String currentUserDir = "data/data/com.cashzhang.ashley/files/";

        RandomAccessFile raf = new RandomAccessFile(currentUserDir + lebel, "rw");
        long totalLen = raf.length();
        FileChannel channel = raf.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, totalLen - 1, 1);
        buffer.put(0, (byte) (']'));

        buffer.force();
        buffer.clear();
        channel.close();
        raf.close();
    }

    private void deleteCategItemFile() {
        File file = new File("data/data/com.cashzhang.ashley/files/");
        File temp = null;
        File[] filelist = file.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            temp = filelist[i];
            if (temp.getName().endsWith(".cif"))
                temp.delete();
        }
    }
}

