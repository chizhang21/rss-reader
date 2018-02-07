package com.cashzhang.ashley;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ListFragment;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import static com.cashzhang.ashley.Constants.s_fragmentFeeds;
import static com.cashzhang.ashley.Constants.s_fragmentManager;

/**
 * Created by hadoop on 02/02/2018.
 */

public class MainFragment extends ListFragment {

    private final static String TAG = "ashley-rss";
    private static final float PULL_DISTANCE = 0.5F;
    private PullToRefreshLayout mPullToRefreshLayout;
    static ListView listViewForDele = null;

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
            Log.d(TAG, "onRefreshStarted: ");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        /*MainActivity activity = (MainActivity) getActivity();
        PullToRefreshLayout layout = (PullToRefreshLayout) inflater.inflate(R.layout.feed_list, container, false);

        Options.Builder optionsBuilder = Options.create();
        optionsBuilder.scrollDistance(PULL_DISTANCE);
        Options options = optionsBuilder.build();

        // Create the ActionBarPullToRefresh object using its SetupWizard.
        ActionBarPullToRefresh.SetupWizard setup = ActionBarPullToRefresh.from(activity);
        setup.allChildrenArePullable();
        setup.options(options);
        setup.useViewDelegate(ListView.class, new FeedListDelegate());
        setup.listener(new RefreshListener(activity));
        setup.setup(layout);

        return layout;*/
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.list_view, container, false);
        TextView emptyView = (TextView) view.findViewById(android.R.id.empty);
        emptyView.setText(R.string.empty_manage_list_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        Options.Builder optionsBuilder = Options.create();
        optionsBuilder.scrollDistance(PULL_DISTANCE);
        Options options = optionsBuilder.build();


        // This is the View which is created by ListFragment
        ViewGroup viewGroup = (ViewGroup) view;

        // We need to create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())

                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)

                // We need to mark the ListView and it's Empty View as pullable
                // This is because they are not dirent children of the ViewGroup
                .theseChildrenArePullable(getListView(), getListView().getEmptyView())

                // We can now complete the setup as desired
                .listener(new RefreshListener(activity))
                .useViewDelegate(ListView.class, new FeedListDelegate())
                .options(options)
                .setup(mPullToRefreshLayout);

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
                MainActivity activity = (MainActivity) getActivity();
                Dialog dialog = DialogEditFeed.newInstance(activity, -1);
                dialog.show();
                Log.d(TAG, "onOptionsItemSelected: ");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
