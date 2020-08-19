package com.cashzhang.nozdormu

import android.app.Application
import android.content.Context
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

    @Volatile
    lateinit var appContext: Context
        private set

    fun initApplication(application: Application) {
        appContext = application
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