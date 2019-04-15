package com.cashzhang.nozdormu.adapter;

import android.util.Log;

import com.cashzhang.nozdormu.fragment.CategsFragment;
import com.cashzhang.nozdormu.fragment.ContentFragment;
import com.cashzhang.nozdormu.fragment.LeftFragment;
import com.cashzhang.nozdormu.fragment.MainFragment;
import com.cashzhang.nozdormu.fragment.SecCategsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by cz21 on 2018/5/23.
 */

public class FrogAdapter extends FragmentStatePagerAdapter {

    private final static String TAG = "nozdormu";
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
