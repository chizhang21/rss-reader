package com.cashzhang.ashley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zhangchi on 2018/2/28.
 */

public class LListAdapter extends BaseAdapter {

    private ArrayList<String> arrayListData;
    private LayoutInflater layoutInflater;

    public final class Component {
        public ImageView image;
        public TextView title;
        public TextView info;
    }

    public LListAdapter(Context context, ArrayList<String> listData) {
        arrayListData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayListData.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListData.get(i);
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
            component.image = (ImageView) convertView.findViewById(R.id.image);
            component.title = (TextView) convertView.findViewById(R.id.title);
            component.info = (TextView) convertView.findViewById(R.id.info);
            convertView.setTag(component);
        } else {
            component = (Component) convertView.getTag();
        }

        component.title.setText((String) arrayListData.get(position));
        component.info.setText((String) arrayListData.get(position));

        return convertView;
    }
}
