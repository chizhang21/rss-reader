package com.cashzhang.nozdormu.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cashzhang.nozdormu.LoginActivity;
import com.cashzhang.nozdormu.Constants;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.Settings;
import com.cashzhang.nozdormu.VolleyController;
import com.cashzhang.nozdormu.bean.Profile;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz21 on 2018/5/22.
 */

public class LeftFragment extends Fragment {

    private final static String TAG = "nozdormu";

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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                String authUrl = Constants.BASE_URL + Constants.AUTH_URL + Constants.RESPONSE_TYPE + Constants.CLIENT_ID + Constants.REDIRECT_URI + Constants.SCOPE;
                String tokenUrl = Constants.BASE_URL + Constants.TOKEN_URL;
                intent.putExtra("authurl", authUrl);
                intent.putExtra("tokenurl", tokenUrl);
                startActivityForResult(intent, 404);
            }
        });

        if (Settings.getEmail() != null) {
            Log.d(TAG, "onCreate: Email=" + Settings.getEmail());
            leftText.setText(Settings.getEmail());
        }
        if (!accessToken.equals("") && !refreshToken.equals("")) {
            Log.d(TAG, "token saved in SharedPreferences");
        }
        return layout;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
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
        StringRequest stringRequest = new StringRequest(Constants.BASE_URL + Constants.PROFILE,
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
    public void onDestroy() {
        super.onDestroy();
    }

}
