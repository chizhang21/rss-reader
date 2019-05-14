package com.cashzhang.nozdormu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.cashzhang.nozdormu.bean.LoginBody;
import com.cashzhang.nozdormu.bean.Token;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cz21 on 2018/6/2.
 */

public class LoginActivity extends Activity {

    private final static String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.WebView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.auth_page);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String authUrl = intent.getStringExtra("authurl");
        final String tokenUrl = intent.getStringExtra("tokenurl");
        final String tokenParams = tokenUrl;
        final FeedlyApi feedlyApi = FeedlyRequest.getInstance();


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("code=")) {
                    String params = url.substring(url.indexOf("?") + 1);
                    String code = "";

                    for (String p : params.split("&")) {
                        if (p.contains("code")) {
                            code = p.split("=")[1];
                            break;
                        }
                    }
                    final String finalCode = code;

//                    Call<Token> loginWithCode = feedlyApi.loginWithCode(new LoginBody(finalCode));

                    feedlyApi.loginWithCode(new LoginBody(finalCode))
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.single())
                            .subscribe(new Observer<Token>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    Log.d(TAG, "onSubscribe: ");
                                }

                                @Override
                                public void onNext(Token token) {
                                    Log.d(TAG, "onNext: token=" + token.toString());
                                    Settings.setAccessToken(token.getAccess_token());
                                    Settings.setRefreshToken(token.getRefresh_token());

                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    LoginActivity.this.finish();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d(TAG, "onError: ");
                                }

                                @Override
                                public void onComplete() {
                                    Log.d(TAG, "onComplete: ");
                                }
                            });
                }
            }
        });
        webView.loadUrl(authUrl);
        webView.getSettings().setUserAgentString("Chrome/56.0.0.0 Mobile");
    }

}
