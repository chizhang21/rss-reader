package com.cashzhang.ashley.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cashzhang.ashley.AuthView;
import com.cashzhang.ashley.Constants;
import com.cashzhang.ashley.R;
import com.cashzhang.ashley.Settings;
import com.cashzhang.ashley.VolleyController;
import com.cashzhang.ashley.bean.Profile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hadoop on 2018/5/22.
 */

public class LeftFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "ashley-rss";

    private static final String BASE_URL = "https://cloud.feedly.com";
    private static final String AUTH_URL = "/v3/auth/auth";
    private static final String TOKEN_URL = "/v3/auth/token";
    private static final String PROFILE = "/v3/profile";
    private static final String CATEGORIES = "/v3/categories?sort=feedly";
    private static final String SUBSCRIPTIONS = "/v3/subscriptions";
    private static final String RESPONSE_TYPE = "?response_type=code";
    private static final String CLIENT_ID = "&client_id=feedly";
    private static final String REDIRECT_URI = "&redirect_uri=https://cloud.feedly.com/feedly.html";
    private static final String SCOPE = "&scope=https://cloud.feedly.com/subscriptions";

    private static String accessToken = null;
    private static String refreshToken = null;

    @BindView(R.id.left_text)
    TextView leftText;
    @BindView(R.id.accounts_img)
    ImageView imageView;

    @BindView(R.id.CATE)
    Button cateBtn;
    @BindView(R.id.SUBS)
    Button subsBtn;
    @BindView(R.id.FEED)
    Button feedBtn;
    @BindView(R.id.ENTRY)
    Button entryBtn;
    @BindView(R.id.STREAM)
    Button streamBtn;

    public static LeftFragment newInstance() {
        LeftFragment leftFragment = new LeftFragment();
        return leftFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.left_page, container, false);
        ButterKnife.bind(this, layout);

        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuthView.class);
                String authUrl = BASE_URL + AUTH_URL + RESPONSE_TYPE + CLIENT_ID + REDIRECT_URI + SCOPE;
                String tokenUrl = BASE_URL + TOKEN_URL;
                intent.putExtra("authurl", authUrl);
                intent.putExtra("tokenurl", tokenUrl);
                startActivityForResult(intent, 404);
            }
        });

        cateBtn.setOnClickListener(this);
        subsBtn.setOnClickListener(this);
        feedBtn.setOnClickListener(this);
        entryBtn.setOnClickListener(this);
        streamBtn.setOnClickListener(this);

        if (Settings.getEmail() != null ) {
            Log.d(TAG, "onCreate: Email=" + Settings.getEmail());
            leftText.setText(Settings.getEmail());
        }
        if (!accessToken.equals("") && !refreshToken.equals("")) {
            Log.d(TAG, "token saved in SharedPreferences");
        }

        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessToken = Settings.getAccessToken();
        Log.d(TAG, "onCreate: accessToken=" + accessToken);
        refreshToken = Settings.getRefreshToken();
        Log.d(TAG, "onCreate: refreshToken=" + refreshToken);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 404)
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "auth success");
                accessToken = Settings.getAccessToken();
                refreshToken = Settings.getRefreshToken();
            }
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Profile profile = JSON.parseObject(response, Profile.class);
                Settings.setId(profile.getId());
                Settings.setEmail(profile.getEmail());
                Settings.setGivenName(profile.getGivenName());
                leftText.setText(Settings.getEmail());
            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        StringRequest stringRequest = new StringRequest(BASE_URL + PROFILE,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", accessToken);
                return headers;
            }
        };
        VolleyController.getInstance(Constants.s_activity).addToRequestQueue(stringRequest);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CATE:
                getCate();
                break;
            case R.id.SUBS:
                getSubs();
                break;
            case R.id.FEED:
                getFeed();
                break;
            case R.id.ENTRY:
                getEntry();
                break;
            case R.id.STREAM:
                getStream();
                break;
            default:
                break;

        }
    }

    public void getCate() {

    }

    public void getSubs() {
        Log.d(TAG, "getSubs: ");
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SUBS: " + response);

            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        StringRequest stringRequest = new StringRequest(BASE_URL + SUBSCRIPTIONS,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", accessToken);
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

    public void getFeed() {
        Log.d(TAG, "getFeed: ");
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "FEED: " + response);

            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        StringRequest stringRequest = new StringRequest(BASE_URL + "/v3/feed/user/bb7abbe1-c7c8-4817-b451-c92a5a4ecbd4/category/IT",
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", accessToken);
                return headers;
            }
        };
        mQueue.add(stringRequest);

    }

    public void getEntry() {
        Log.d(TAG, "getEntry: ");
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "ENTRY: " + response);

            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        StringRequest stringRequest = new StringRequest(BASE_URL + "/v3/entries/user/bb7abbe1-c7c8-4817-b451-c92a5a4ecbd4/category/IT",
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", accessToken);
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

    public void getStream() {
        Log.d(TAG, "getStream: ");
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "STREAM: " + response);

            }
        };
        //error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        String input = null;
        try {
            input = BASE_URL + "/v3/streams/contents?streamId="+ URLEncoder.encode("user/bb7abbe1-c7c8-4817-b451-c92a5a4ecbd4/category/IT", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(input,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Feedly-Access-Token", accessToken);
                return headers;
            }
        };
        VolleyController.getInstance(Constants.s_activity).addToRequestQueue(stringRequest);
    }

}
