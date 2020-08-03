package com.cashzhang.nozdormu

import android.content.res.Resources
import android.widget.ListView
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cashzhang.nozdormu.fragment.FeedsFragment
import com.cashzhang.nozdormu.fragment.StreamsFragment
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

/**
 * Created by cz21 on 2018/2/6.
 */
object Constants {
    const val BASE_URL = "https://cloud.feedly.com"
    const val AUTH_URL = "/v3/auth/auth"
    const val TOKEN_URL = "/v3/auth/token"
    const val PROFILE = "/v3/profile"
    const val CATEGORIES = "/v3/categories?sort=feedly"
    const val SUBSCRIPTIONS = "/v3/subscriptions"
    const val RESPONSE_TYPE = "?response_type=code"
    const val CLIENT_ID = "&client_id=feedly"
    const val REDIRECT_URI = "&redirect_uri=https://cloud.feedly.com/feedly.html"
    const val SCOPE = "&scope=https://cloud.feedly.com/subscriptions"
    var s_activity: MainActivity? = null
    var s_resources: Resources? = null
    var s_fragmentFeeds: StreamsFragment? = null
    var s_fragmentSecCateg: FeedsFragment? = null
    var s_fragmentManager: FragmentManager? = null
    var s_listView: ListView? = null
    var s_swipeSLayout: SwipeRefreshLayout? = null
    var s_swipeCLayout: SwipeRefreshLayout? = null
    var s_swipeMLayout: SwipeRefreshLayout? = null
    fun saveInitialConstants(activity: MainActivity) {
        s_activity = activity
        s_resources = activity.resources
        //        s_listView = activity.findViewById(R.id.l_list);
        s_fragmentManager = activity.supportFragmentManager
    }

    fun getFragmentView(streamsFragment: StreamsFragment?) {
        s_fragmentFeeds = streamsFragment
        s_swipeMLayout = s_fragmentFeeds!!.view!!.findViewById(R.id.swipe_refresh)
    }

    fun getSecCatesFragmentView(feedsFragment: FeedsFragment?) {
        s_fragmentSecCateg = feedsFragment
        s_swipeSLayout = s_fragmentSecCateg!!.view!!.findViewById(R.id.swipe_refresh)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun createXmlParser(urlString: CharSequence): XmlPullParser {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        val url = URL(urlString.toString())
        val inputStream = url.openStream()
        parser.setInput(inputStream, null)
        return parser
    }

    fun tmpWrite(fileName: String?, response: String) {
        val currentUserDir = "data/data/com.cashzhang.nozdormu/files/"
        val file = File(currentUserDir, fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file, false)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(response.toByteArray())
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}