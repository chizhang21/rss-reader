package com.cashzhang.nozdormu.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.bean.Item;
import com.cashzhang.nozdormu.fragment.StreamsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.StreamViewHolder> {

    private final static String TAG = StreamAdapter.class.getSimpleName();
    private ArrayList<Item> itemList;
    private static ClickListener clickListener;

    public static class StreamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        /*@BindView(R.id.webtitle)*/ TextView webTitle;
        /*@BindView(R.id.timestamp)*/ TextView timestamp;
        /*@BindView(R.id.stream_title)*/ TextView streamTitle;
        /*@BindView(R.id.stream_content)*/ TextView streamContent;

        public StreamViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            webTitle = v.findViewById(R.id.webtitle);
            timestamp = v.findViewById(R.id.timestamp);
            streamTitle = v.findViewById(R.id.stream_title);
            streamContent = v.findViewById(R.id.stream_content);
//            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public StreamAdapter(ArrayList<Item> dataSet) {
        this.itemList = dataSet;
    }

    @NonNull
    @Override
    public StreamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_item, null, false);
        StreamViewHolder labelViewHolder = new StreamViewHolder(v);

        return labelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StreamViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position="+position);
        holder.webTitle.setText(itemList.get(position).getOrigin().getTitle());
        holder.streamTitle.setText(itemList.get(position).getTitle());
        holder.timestamp.setText(StreamsFragment.longToString(itemList.get(position).getPublished(), "MM-dd HH:mm"));
        holder.streamContent.setText(itemList.get(position).getSummary().getContent());
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void refreshData(ArrayList<Item> items) {
        Log.d(TAG, "refreshData: ");
        itemList = items;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        StreamAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
