package com.cashzhang.ashley;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cashzhang.ashley.Constants.s_fragmentFeeds;
import static com.cashzhang.ashley.Constants.s_fragmentManager;
import static com.cashzhang.ashley.Constants.s_swipeLayout;
import static com.cashzhang.ashley.Constants.saveInitialConstants;


public class MainActivity extends AppCompatActivity {

    public FragmentSwitch fragmentSwitch;
    List<Fragment> fragments;
    FrogAdapter adapter;
    static Bundle bundle;
    public List<IndexItem> m_index;
    static final String INDEX = "index.txt";

    LeftFragment leftFragment = LeftFragment.newInstance();
    MainFragment mainFragment = MainFragment.newInstance();
    ContentFragment contentFragment = ContentFragment.newInstance();

    @BindView(R.id.toolBar) Toolbar myToolbar;
    @BindView(R.id.viewpager) ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveInitialConstants(this);
        ButterKnife.bind(this);

        fragments = new ArrayList<Fragment>();
        fragments.add(leftFragment);
        fragments.add(mainFragment);
        fragments.add(contentFragment);

        adapter = new FrogAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        /*getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new MainFragment())
                .commit();*/


        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
//        ab.setDisplayHomeAsUpEnabled(true);

        // Load the index from file.
        ObjectIO indexReader = new ObjectIO(this, INDEX);
        m_index = (List<IndexItem>) indexReader.read();

        if (null == m_index) {
            m_index = new ArrayList<IndexItem>();
        }

        if (null == savedInstanceState) {
            s_fragmentManager.executePendingTransactions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (s_swipeLayout.isRefreshing()) {
                    s_swipeLayout.setRefreshing(false);
                    return true;
                } else
                    break;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public interface FragmentSwitch {
        void gotoFragment(ViewPager viewPager, FrogAdapter adapter);
    }

    public void setFragmentSwitch(FragmentSwitch fragmentSwitch) {
        this.fragmentSwitch = fragmentSwitch;
    }
    public void forSkip() {
        if (fragmentSwitch != null) {
            fragmentSwitch.gotoFragment(vp, adapter);
        }
    }

    public void setBundle(Bundle bundle){
        this.bundle = bundle;
    }
    public static Bundle getBundle(){
        return bundle;
    }
}

