package com.cashzhang.ashley;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by zhangchi on 2018/5/23.
 */

public class FrogAdapter extends FragmentStatePagerAdapter {

    private final static String TAG = "ashley-rss";
    private List<Fragment> mFragments;

    public FrogAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        // TODO Auto-generated constructor stub
        mFragments=fragments;
    }
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + position);
        switch (position) {
            case 0:
                return LeftFragment.newInstance();
            case 1:
                return CategsFragment.newInstance();
            case 2:
                return SecCategsFragment.newInstance();
            case 3:
                return MainFragment.newInstance();
            case 4:
                return ContentFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
