package com.cashzhang.nozdormu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import butterknife.BindView
import butterknife.ButterKnife
import com.cashzhang.nozdormu.model.LoginBody
import com.cashzhang.nozdormu.model.Token
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by cz21 on 2018/6/2.
 */
class LoginActivity : Activity() {
    @BindView(R.id.WebView)
    var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.auth_page)
        ButterKnife.bind(this)
        val intent = intent
        val authUrl = intent.getStringExtra("authurl")
        val tokenUrl = intent.getStringExtra("tokenurl")
        val tokenParams = tokenUrl
        val feedlyApi: FeedlyApi = FeedlyRequest.Companion.getInstance()
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
                    val finalCode = code

//                    Call<Token> loginWithCode = feedlyApi.loginWithCode(new LoginBody(finalCode));
                    feedlyApi.loginWithCode(LoginBody(finalCode))
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.single())
                            .subscribe(object : Observer<Token> {
                                override fun onSubscribe(d: Disposable) {
                                    Log.d(TAG, "onSubscribe: ")
                                }

                                override fun onNext(token: Token) {
                                    Log.d(TAG, "onNext: token=$token")
                                    Settings.setAccessToken(token.access_token)
                                    Settings.setRefreshToken(token.refresh_token)
                                    val intent = Intent()
                                    setResult(RESULT_OK, intent)
                                    finish()
                                }

                                override fun onError(e: Throwable) {
                                    Log.d(TAG, "onError: ")
                                }

                                override fun onComplete() {
                                    Log.d(TAG, "onComplete: ")
                                }
                            })
                }
            }
        }
        webView!!.loadUrl(authUrl)
        webView!!.settings.userAgentString = "Chrome/56.0.0.0 Mobile"
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}