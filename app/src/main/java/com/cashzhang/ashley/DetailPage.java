package com.cashzhang.ashley;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

/**
 * Created by hadoop on 27/03/2018.
 */

public class DetailPage extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);
        WebView wbView = (WebView) findViewById(R.id.WebView);
        wbView.getSettings().setJavaScriptEnabled(true);
        wbView.loadUrl("http://www.google.com");
    }
}
