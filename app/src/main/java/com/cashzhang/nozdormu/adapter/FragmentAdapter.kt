package com.cashzhang.nozdormu.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.cashzhang.nozdormu.fragment.*

class FragmentAdapter // TODO Auto-generated constructor stub
(fm: FragmentManager?, private val mFragments: List<Fragment>) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        Log.d(TAG, "getItem: $position")
        return when (position) {
            0 -> LeftFragment.newInstance()
            1 -> CollectionsFragment.newInstance()
            2 -> FeedsFragment.newInstance()
            3 -> StreamsFragment.newInstance()
            else -> ContentFragment.newInstance()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    companion object {
        private val TAG = FragmentAdapter::class.java.simpleName
    }

}