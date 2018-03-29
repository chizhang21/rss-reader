package com.cashzhang.ashley;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by zhangchi on 2018/2/6.
 */

public class Constants {

    static MainActivity s_activity;
    static Resources s_resources;
    static MainFragment s_fragmentFeeds;
    static FragmentManager s_fragmentManager;
    static ListView s_listView;
    static SwipeRefreshLayout s_swipeLayout;
    static int s_eightDp;
    static DisplayMetrics s_displayMetrics;

    static void saveInitialConstants(MainActivity activity) {
        s_activity = activity;
        s_resources = activity.getResources();
        s_listView = activity.findViewById(R.id.l_list);
        s_fragmentManager = activity.getFragmentManager();
        s_displayMetrics = s_resources.getDisplayMetrics();
        s_swipeLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh);
        s_fragmentFeeds = (MainFragment) s_fragmentManager.findFragmentById(R.id.main_fragment);

        s_eightDp = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0F, s_displayMetrics));
    }

    static void hideFragments(Fragment... fragments) {

    }

    static void showFragments(Fragment... fragments) {

    }

    public static XmlPullParser createXmlParser(CharSequence urlString) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();

        URL url = new URL(urlString.toString());
        InputStream inputStream = url.openStream();
        parser.setInput(inputStream, null);
        return parser;
    }
}
