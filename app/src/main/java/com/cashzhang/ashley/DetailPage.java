package com.cashzhang.ashley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hadoop on 27/03/2018.
 */

public class DetailPage extends Activity {

    @BindView(R.id.WebView) WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.detail_page);
        ButterKnife.bind(this);
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.loadUrl(url);
    }
}
