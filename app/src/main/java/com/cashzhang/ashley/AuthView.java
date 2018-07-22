package com.cashzhang.ashley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hadoop on 2018/6/2.
 */

public class AuthView extends Activity {

    private final static String TAG = "ashley-rss";

    @BindView(R.id.WebView)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
                    //success listener
                    Response.Listener listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            Token token = JSON.parseObject(response, Token.class);

                            Settings.setAccessToken(token.getAccess_token());
                            Settings.setRefreshToken(token.getRefresh_token());

                            Intent intent = new Intent();
//                            intent.putExtra("accessToken", token.getAccess_token());
//                            intent.putExtra("refreshToken", token.getRefresh_token());
                            setResult(Activity.RESULT_OK, intent);
                            AuthView.this.finish();
                        }
                    };
                    //error listener
                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.getMessage(), error);
                        }
                    };
                    //POST request
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, tokenParams,
                            listener, errorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("Content-Type", "application/json");
                            map.put("client_id", "feedly");
                            map.put("client_secret", "0XP4XQ07VVMDWBKUHTJM4WUQ");
                            map.put("grant_type", "authorization_code");
                            map.put("redirect_uri", "https://cloud.feedly.com/feedly.html");
                            map.put("code", finalCode);
                            return map;
                        }
                    };
                    VolleyController.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });
        webView.loadUrl(authUrl);
        webView.getSettings().setUserAgentString("Chrome/56.0.0.0 Mobile");
    }

}
