package com.cashzhang.nozdormu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cashzhang.nozdormu.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategListAdapter extends BaseAdapter {
    private final static String TAG = "nozdormu";


    private ArrayList<String> arrayListLabel;

    private LayoutInflater layoutInflater;

    static class Component {
        @BindView(R.id.categ_content) TextView content;
        public Component(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public CategListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData( ArrayList<String> listContent) {
        arrayListLabel = listContent;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ((arrayListLabel == null) ? 0 :arrayListLabel.size());
    }

    @Override
    public Object getItem(int i) {
        return ((arrayListLabel == null) ? null : arrayListLabel.get(i));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategListAdapter.Component component;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.categ_item, null);
            component = new CategListAdapter.Component(convertView);
            convertView.setTag(component);
        } else {
            component = (CategListAdapter.Component) convertView.getTag();
        }
        component.content.setText((arrayListLabel == null) ? null : arrayListLabel.get(position));

        return convertView;
    }
}
