package com.cashzhang.ashley;

import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by zhangchi on 2018/2/6.
 */

public class Constants {

    static MainFragment s_fragmentFeeds;
    static FragmentManager s_fragmentManager;
    static DrawerLayout s_drawerLayout;

    static void saveInitialConstants(MainActivity activity) {
        s_fragmentManager = activity.getFragmentManager();
        s_fragmentFeeds = (MainFragment) s_fragmentManager.findFragmentById(R.id.main_fragment);
        s_drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
    }
}
