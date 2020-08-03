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
import com.cashzhang.nozdormu.adapter.StreamAdapter.StreamViewHolder
import com.cashzhang.nozdormu.fragment.StreamsFragment
import com.cashzhang.nozdormu.model.Item
import java.util.*

class StreamAdapter(private var itemList: ArrayList<Item>) : RecyclerView.Adapter<StreamViewHolder>() {

    class StreamViewHolder(v: View) : ViewHolder(v), View.OnClickListener, OnLongClickListener {
        var webTitle: TextView
        var timestamp: TextView
        var streamTitle: TextView
        var streamContent: TextView
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
            webTitle = v.findViewById(R.id.webtitle)
            timestamp = v.findViewById(R.id.timestamp)
            streamTitle = v.findViewById(R.id.stream_title)
            streamContent = v.findViewById(R.id.stream_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stream_item, null, false)
        return StreamViewHolder(v)
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: position=$position")
        holder.webTitle.text = itemList[position].origin.title
        holder.streamTitle.text = itemList[position].title
        holder.timestamp.text = StreamsFragment.longToString(itemList[position].published, "MM-dd HH:mm")
        holder.streamContent.text = itemList[position].summary.content
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun refreshData(items: ArrayList<Item>) {
        Log.d(TAG, "refreshData: ")
        itemList = items
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
        private val TAG = StreamAdapter::class.java.simpleName
        private var clickListener: ClickListener? = null
    }

}