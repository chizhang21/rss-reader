package com.cashzhang.nozdormu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cashzhang.nozdormu.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionsListAdapter extends BaseAdapter {
    private final static String TAG = CollectionsListAdapter.class.getSimpleName();


    private ArrayList<String> collectionsList;

    private LayoutInflater layoutInflater;

    static class Component {
        @BindView(R.id.collection_content) TextView content;

        public Component(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public CollectionsListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(ArrayList<String> collectionContent) {
        Log.d(TAG, "refreshData: ");
        collectionsList = collectionContent;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ((collectionsList == null) ? 0 :collectionsList.size());
    }

    @Override
    public Object getItem(int i) {
        return ((collectionsList == null) ? null : collectionsList.get(i));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CollectionsListAdapter.Component component;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.collection_item, null);
            component = new CollectionsListAdapter.Component(convertView);
            convertView.setTag(component);
        } else {
            component = (CollectionsListAdapter.Component) convertView.getTag();
        }
        component.content.setText((collectionsList == null) ? null : collectionsList.get(position));

        return convertView;
    }
}
