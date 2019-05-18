package com.cashzhang.nozdormu.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashzhang.nozdormu.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.LabelViewHolder> {

    private final static String TAG = FeedAdapter.class.getSimpleName();
    private ArrayList<String> labelList;
    private static ClickListener clickListener;

    public static class LabelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.label_content) TextView content;
        public LabelViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
//            content = v.findViewById(R.id.label_content);
            ButterKnife.bind(this, v);
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

    public FeedAdapter(ArrayList<String> dataSet) {
        this.labelList = dataSet;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, null, false);
        LabelViewHolder labelViewHolder = new LabelViewHolder(v);

        return labelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position="+position);
        holder.content.setText(labelList.get(position));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    public void refreshData(ArrayList<String> labelContent) {
        Log.d(TAG, "refreshData: ");
        labelList = labelContent;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        FeedAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
