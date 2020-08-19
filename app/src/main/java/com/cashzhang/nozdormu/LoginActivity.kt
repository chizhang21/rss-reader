package com.cashzhang.nozdormu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cashzhang.nozdormu.model.LoginBody
import com.cashzhang.nozdormu.network.FeedlyService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.kotlin.subscribeBy

/**
 * Created by cz21 on 2018/6/2.
 */
class LoginActivity : Activity() {

    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = this.findViewById(R.id.webView)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.auth_page)
        val intent = intent
        val authUrl = intent.getStringExtra("authurl")
        val tokenUrl = intent.getStringExtra("tokenurl")
        val tokenParams = tokenUrl
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                if (url.contains("code=")) {
                    val params = url.substring(url.indexOf("?") + 1)
                    var code = ""
                    for (p in params.split("&".toRegex()).toTypedArray()) {
                        if (p.contains("code")) {
                            code = p.split("=".toRegex()).toTypedArray()[1]
                            break
                        }
                    }

                    FeedlyService.INSTANCE.loginWithCode(LoginBody(code))
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onSuccess = { token ->
                                Log.d("zhangchi", "onSuccess: token=$token")
                                Settings.accessToken = token.access_token
                                Settings.refreshToken = token.refresh_token
                                val intent = Intent()
                                setResult(RESULT_OK, intent)
                                finish()
                            },
                            onError = {

                            }
                        )
                }
            }
        }
        webView?.loadUrl(authUrl)
        webView?.settings?.userAgentString = "Chrome/56.0.0.0 Mobile"
    }
}