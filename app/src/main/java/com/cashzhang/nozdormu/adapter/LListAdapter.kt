package com.cashzhang.nozdormu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.cashzhang.nozdormu.R
import java.util.*

class LListAdapter(context: Context?) : BaseAdapter() {
    private var arrayListTitle: ArrayList<String>? = null
    private var arrayListData: ArrayList<String>? = null
    private var arrayListContent: ArrayList<String>? = null
    private var arrayListTime: ArrayList<String>? = null
    private val layoutInflater: LayoutInflater

    internal class Component(view: View?) {
        @BindView(R.id.webtitle)
        var webTitle: TextView? = null

        @BindView(R.id.title)
        var title: TextView? = null

        @BindView(R.id.content)
        var content: TextView? = null

        @BindView(R.id.timestamp)
        var timestamp: TextView? = null

        init {
            ButterKnife.bind(this, view!!)
        }
    }

    fun refreshData(listTitle: ArrayList<String>?,
                    listData: ArrayList<String>?,
                    listContent: ArrayList<String>?,
                    listTime: ArrayList<String>?
    ) {
        arrayListTitle = listTitle
        arrayListData = listData
        arrayListContent = listContent
        arrayListTime = listTime
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return if (arrayListData == null) 0 else arrayListData!!.size
    }

    override fun getItem(i: Int): Any {
        return arrayListData!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        val component: Component
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.stream_item, null)
            component = Component(convertView)
            convertView.tag = component
        } else {
            component = convertView.tag as Component
        }
        component.webTitle!!.text = if (arrayListTitle == null) null else arrayListTitle!![position]
        component.title!!.text = if (arrayListData == null) null else arrayListData!![position]
        component.content!!.text = if (arrayListContent == null) null else arrayListContent!![position]
        component.timestamp!!.text = if (arrayListTime == null) null else arrayListTime!![position]
        return convertView
    }

    companion object {
        private val TAG = LListAdapter::class.java.simpleName
    }

    init {
        layoutInflater = LayoutInflater.from(context)
    }
}