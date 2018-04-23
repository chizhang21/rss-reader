package com.cashzhang.ashley;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangchi on 2018/2/28.
 */

public class LListAdapter extends BaseAdapter {

    private final static String TAG = "ashley-rss";
    private ArrayList<String> arrayListTitle;
    private ArrayList<String> arrayListData;
    private ArrayList<String> arrayListContent;
    private ArrayList<String> arrayListTime;
    private LayoutInflater layoutInflater;

    static class Component {
        @BindView(R.id.webtitle) TextView webTitle;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.timestamp) TextView timestamp;
        public Component(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public LListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(ArrayList<String> listTitle,
                            ArrayList<String> listData,
                            ArrayList<String> listContent,
                            ArrayList<String> listTime
                            ) {
        arrayListTitle = listTitle;
        arrayListData = listData;
        arrayListContent = listContent;
        arrayListTime = listTime;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ((arrayListData == null) ? 0 :arrayListData.size());
    }

    @Override
    public Object getItem(int i) {
        return ((arrayListData == null) ? null : arrayListData.get(i));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Component component;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.feed_item, null);
            component = new Component(convertView);
            convertView.setTag(component);
        } else {
            component = (Component) convertView.getTag();
        }
        component.webTitle.setText((arrayListTitle == null) ? null : arrayListTitle.get(position));
        component.title.setText((arrayListData == null) ? null : arrayListData.get(position));
        component.content.setText((arrayListContent == null) ? null : arrayListContent.get(position));
        component.timestamp.setText((arrayListTime == null) ? null : arrayListTime.get(position));

        return convertView;
    }



}
