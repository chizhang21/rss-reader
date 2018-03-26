package com.cashzhang.ashley;

import android.content.Context;
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

public class LListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

    private ArrayList<String> arrayListData;
    private ArrayList<String> arrayListUrl;
    private LayoutInflater layoutInflater;
    private final static String TAG = "ashley-rss";

    public final class Component {
//        public ImageView image;
        public TextView title;
//        public TextView info;
    }

    public LListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(ArrayList<String> listData, ArrayList<String> listUrl) {
        arrayListData = listData;
        arrayListUrl = listUrl;
        notifyDataSetChanged();
    }

    private String getUrl(int i) {
        return ((arrayListUrl == null) ? null : arrayListUrl.get(position));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick: " + position + " url: " + getUrl(position));
        //TODO
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
//            component.image = (ImageView) convertView.findViewById(R.id.image);
            component.title = (TextView) convertView.findViewById(R.id.title);
//            component.info = (TextView) convertView.findViewById(R.id.info);
            convertView.setTag(component);
        } else {
            component = (Component) convertView.getTag();
        }
        component.title.setText((arrayListData == null) ? null : arrayListData.get(position));
//        component.info.setText((String) arrayListData.get(position));

        return convertView;
    }

}
