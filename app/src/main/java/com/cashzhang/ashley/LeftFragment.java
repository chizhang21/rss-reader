package com.cashzhang.ashley;

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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hadoop on 2018/5/22.
 */

public class LeftFragment extends Fragment {

    private final static String TAG = "ashley-rss";

    private static final String BASE_URL = "https://cloud.feedly.com";
    private static final String AUTH_URL = "/v3/auth/auth";
    private static final String TOKEN_URL = "/v3/auth/token";
    private static final String PROFILE = "/v3/profile";
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
                Intent intent = new Intent(getActivity(),AuthView.class);
                String authUrl = BASE_URL + AUTH_URL + RESPONSE_TYPE + CLIENT_ID + REDIRECT_URI + SCOPE;
                String tokenUrl = BASE_URL + TOKEN_URL;
                intent.putExtra("authurl", authUrl);
                intent.putExtra("tokenurl", tokenUrl);
                startActivityForResult(intent, 404);
            }
        });

        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 404)
            if (resultCode == Activity.RESULT_OK) {
                //TODO login success
                Log.d(TAG, "onActivityResult: RESULT_OK");
                accessToken = data.getStringExtra("accessToken");
                refreshToken = data.getStringExtra("refreshToken");
            }
        RequestQueue mQueue = Volley.newRequestQueue(Constants.s_activity);
        //create listener
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        //create error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        //GET request
        StringRequest stringRequest = new StringRequest(BASE_URL+PROFILE,
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

        super.onActivityResult(requestCode, resultCode, data);
    }
}
