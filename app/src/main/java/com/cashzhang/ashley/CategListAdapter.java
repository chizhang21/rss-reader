package com.cashzhang.ashley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategListAdapter extends BaseAdapter {
    private final static String TAG = "ashley-rss";


    private ArrayList<String> arrayListContent;

    private LayoutInflater layoutInflater;

    static class Component {
        @BindView(R.id.content) TextView content;
        @BindView(R.id.timestamp) TextView timestamp;
        public Component(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public CategListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(
                            ArrayList<String> listContent
    ) {
        arrayListContent = listContent;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ((arrayListContent == null) ? 0 :arrayListContent.size());
    }

    @Override
    public Object getItem(int i) {
        return ((arrayListContent == null) ? null : arrayListContent.get(i));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LListAdapter.Component component;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.feed_item, null);
            component = new LListAdapter.Component(convertView);
            convertView.setTag(component);
        } else {
            component = (LListAdapter.Component) convertView.getTag();
        }
        component.content.setText((arrayListContent == null) ? null : arrayListContent.get(position));

        return convertView;
    }
}
