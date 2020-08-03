package com.cashzhang.nozdormu.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cashzhang.nozdormu.R
import com.cashzhang.nozdormu.adapter.FeedAdapter.FeedViewHolder
import java.util.*

class FeedAdapter(private var labelList: ArrayList<String>) : RecyclerView.Adapter<FeedViewHolder>() {

    class FeedViewHolder(v: View) : ViewHolder(v), View.OnClickListener, OnLongClickListener {
        /*@BindView(R.id.label_content)*/
        var content: TextView
        override fun onClick(v: View) {
            clickListener!!.onItemClick(adapterPosition, v)
        }

        override fun onLongClick(v: View): Boolean {
            clickListener!!.onItemLongClick(adapterPosition, v)
            return false
        }

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
            content = v.findViewById(R.id.label_content)
            //            ButterKnife.bind(this, v);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, null, false)
        return FeedViewHolder(v)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: position=$position")
        holder.content.text = labelList[position]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getItemCount(): Int {
        return labelList.size
    }

    fun refreshData(labelContent: ArrayList<String>) {
        Log.d(TAG, "refreshData: ")
        labelList = labelContent
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener?) {
        Companion.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }

    companion object {
        private val TAG = FeedAdapter::class.java.simpleName
        private var clickListener: ClickListener? = null
    }

}