package com.cashzhang.ashley;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by hadoop on 02/02/2018.
 */

public class MainFragment extends Fragment {

    private static class RefreshListener implements OnRefreshListener {
        private final MainActivity m_activity;

        RefreshListener(MainActivity activity) {
            m_activity = activity;
        }

        @Override
        public void onRefreshStarted(View view) {
            Intent intent = new Intent(m_activity, ServiceUpdate.class);
            //intent.putExtra(EXTRA_PAGE_NAME, s_viewPager.getCurrentItem());
            m_activity.startService(intent);
        }
    }
    private final String TAG = "ashley-rss";
    private static final float PULL_DISTANCE = 0.5F;
    static final String EXTRA_PAGE_NAME = "GROUP_NUMBER";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        PullToRefreshLayout layout = (PullToRefreshLayout) inflater.inflate(R.layout.feed_list, container, false);

        Options.Builder optionsBuilder = Options.create();
        optionsBuilder.scrollDistance(PULL_DISTANCE);
        Options options = optionsBuilder.build();

        // Create the ActionBarPullToRefresh object using its SetupWizard.
        ActionBarPullToRefresh.SetupWizard setup = ActionBarPullToRefresh.from(activity);
        setup.allChildrenArePullable();
        setup.options(options);
//        setup.useViewDelegate(ViewPager.class, new ViewPagerDelegate());
        setup.listener(new RefreshListener(activity));
        setup.setup(layout);

        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_feeds, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_feed:
//                newGame();
                Log.d(TAG, "onOptionsItemSelected: ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static ListView getTagListView() {
        return getListFragment().getListView();
    }

    static ListFragmentTag getListFragment(int page) {
        String tag = "android:switcher:" + R.id.viewpager + ':' + page;
        return (ListFragmentTag) s_fragmentManager.findFragmentByTag(tag);
    }

}
