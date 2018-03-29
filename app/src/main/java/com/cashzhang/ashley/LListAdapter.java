package com.cashzhang.ashley;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zhangchi on 2018/2/28.
 */

public class LListAdapter extends BaseAdapter {

    private ArrayList<String> arrayListTitle;
    private ArrayList<String> arrayListData;
    private LayoutInflater layoutInflater;
    public static final String BROADCAST_ACTION = "com.cashzhang.llistadapter.handle";
    private final static String TAG = "ashley-rss";

    public final class Component {
        public TextView webTitle;
        public TextView title;
    }

    public LListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(ArrayList<String> listTitle, ArrayList<String> listData) {
        arrayListTitle = listTitle;
        arrayListData = listData;
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
        Component component = null;
        if (convertView == null) {
            component = new Component();
            convertView = layoutInflater.inflate(R.layout.feed_item, null);
            component.webTitle = (TextView) convertView.findViewById(R.id.webtitle);
            component.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(component);
        } else {
            component = (Component) convertView.getTag();
        }
        component.webTitle.setText((arrayListTitle == null) ? null : arrayListTitle.get(position));
        component.title.setText((arrayListData == null) ? null : arrayListData.get(position));

        Log.d(TAG, "position = " + position + ". count-1 = " + (getCount() - 1));

        return convertView;
    }

}
