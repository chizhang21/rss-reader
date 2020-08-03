package com.cashzhang.nozdormu

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.cashzhang.nozdormu.adapter.FragmentAdapter
import com.cashzhang.nozdormu.fragment.*
import com.cashzhang.nozdormu.service.UpdateService
import java.util.*

class MainActivity : AppCompatActivity() {
    var fragmentSwitch: FragmentSwitch? = null
    var fragments: MutableList<Fragment>? = null
    var adapter: FragmentAdapter? = null
    var leftFragment = LeftFragment.newInstance()
    var collectionsFragment = CollectionsFragment.newInstance()
    var feedsFragment = FeedsFragment.newInstance()
    var streamsFragment = StreamsFragment.newInstance()
    var contentFragment = ContentFragment.newInstance()

    //
    //    @BindView(R.id.toolBar)
    //    Toolbar myToolbar;
    @BindView(R.id.viewpager)
    var vp: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Constants.saveInitialConstants(this)
        ButterKnife.bind(this)
        fragments = ArrayList()
        fragments.add(leftFragment)
        fragments.add(collectionsFragment)
        fragments.add(feedsFragment)
        fragments.add(streamsFragment)
        fragments.add(contentFragment)
        adapter = FragmentAdapter(supportFragmentManager, fragments)
        vp!!.adapter = adapter
        //
//        setSupportActionBar(myToolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayShowTitleEnabled(false);
        if (null == savedInstanceState) {
            Constants.s_fragmentManager!!.executePendingTransactions()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Settings.getAccessToken() != null) startService(Intent(this, UpdateService::class.java))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (Constants.s_swipeCLayout != null && Constants.s_swipeCLayout!!.isRefreshing) {
                    Constants.s_swipeCLayout!!.isRefreshing = false
                } else if (Constants.s_swipeSLayout != null && Constants.s_swipeSLayout!!.isRefreshing) {
                    Constants.s_swipeSLayout!!.isRefreshing = false
                } else if (Constants.s_swipeMLayout != null && Constants.s_swipeMLayout!!.isRefreshing) {
                    Constants.s_swipeMLayout!!.isRefreshing = false
                }
                return true
            }
            else -> onBackPressed()
        }
        return super.dispatchKeyEvent(event)
    }

    interface FragmentSwitch {
        fun gotoFragment(viewPager: ViewPager?, adapter: FragmentAdapter?)
    }

    fun setFragmentSwitch(fragmentSwitch: FragmentSwitch?) {
        this.fragmentSwitch = fragmentSwitch
    }

    fun forSkip() {
        if (fragmentSwitch != null) {
            fragmentSwitch!!.gotoFragment(vp, adapter)
        }
    }

    fun setBundle(bundle: Bundle) {
        this.bundle = bundle
    }

    companion object {
        @JvmStatic
        var bundle: Bundle? = null
    }
}