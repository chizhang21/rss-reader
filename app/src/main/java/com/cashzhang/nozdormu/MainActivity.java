package com.cashzhang.nozdormu;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.cashzhang.nozdormu.adapter.FrogAdapter;
import com.cashzhang.nozdormu.fragment.CategsFragment;
import com.cashzhang.nozdormu.fragment.ContentFragment;
import com.cashzhang.nozdormu.fragment.LeftFragment;
import com.cashzhang.nozdormu.fragment.MainFragment;
import com.cashzhang.nozdormu.fragment.SecCategsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cashzhang.nozdormu.Constants.s_fragmentManager;
import static com.cashzhang.nozdormu.Constants.s_swipeMLayout;
import static com.cashzhang.nozdormu.Constants.s_swipeCLayout;
import static com.cashzhang.nozdormu.Constants.s_swipeSLayout;
import static com.cashzhang.nozdormu.Constants.saveInitialConstants;


public class MainActivity extends AppCompatActivity {

    public FragmentSwitch fragmentSwitch;
    List<Fragment> fragments;
    FrogAdapter adapter;
    static Bundle bundle;
    public List<IndexItem> m_index;
    public static final String INDEX = "index.txt";

    LeftFragment leftFragment = LeftFragment.newInstance();
    CategsFragment categsFragment = CategsFragment.newInstance();
    SecCategsFragment secCategsFragment = SecCategsFragment.newInstance();
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
        fragments.add(categsFragment);
        fragments.add(secCategsFragment);
        fragments.add(mainFragment);
        fragments.add(contentFragment);

        adapter = new FrogAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);

        // Load the index from file.
//        ObjectIO indexReader = new ObjectIO(this, INDEX);
//        m_index = (List<IndexItem>) indexReader.read();
//
//        if (null == m_index) {
//            m_index = new ArrayList<IndexItem>();
//        }

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

                if (s_swipeCLayout != null && s_swipeCLayout.isRefreshing()) {
                    s_swipeCLayout.setRefreshing(false);
                } else if (s_swipeSLayout != null && s_swipeSLayout.isRefreshing()) {
                    s_swipeSLayout.setRefreshing(false);
                } else if (s_swipeMLayout != null && s_swipeMLayout.isRefreshing()) {
                    s_swipeMLayout.setRefreshing(false);
                }
                return true;
            default:
                this.onBackPressed();
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

