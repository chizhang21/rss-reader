package com.cashzhang.ashley;

import android.annotation.TargetApi;
import android.app.Activity;

import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    static CategsFragment s_fragmentCateg;
    static SecCategsFragment s_fragmentSecCateg;
    static FragmentManager s_fragmentManager;
    static ListView s_listView;
    static SwipeRefreshLayout s_swipeSLayout;
    static SwipeRefreshLayout s_swipeCLayout;
    static SwipeRefreshLayout s_swipeMLayout;

    static void saveInitialConstants(MainActivity activity) {
        s_activity = activity;
        s_resources = activity.getResources();
        s_listView = activity.findViewById(R.id.l_list);
        s_fragmentManager = activity.getSupportFragmentManager();
    }

    static public void getFragmentView(MainFragment mainFragment) {
        s_fragmentFeeds = mainFragment;
        s_swipeMLayout = (SwipeRefreshLayout) s_fragmentFeeds.getView().findViewById(R.id.swipe_refresh);
    }

    static public void getCatesFragmentView(CategsFragment categsFragment) {
        s_fragmentCateg = categsFragment;
        s_swipeCLayout = (SwipeRefreshLayout) s_fragmentCateg.getView().findViewById(R.id.swipe_refresh);
    }

    static public void getSecCatesFragmentView(SecCategsFragment secCategsFragment) {
        s_fragmentSecCateg = secCategsFragment;
        s_swipeSLayout = (SwipeRefreshLayout) s_fragmentSecCateg.getView().findViewById(R.id.swipe_refresh);
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
