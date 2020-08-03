package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cashzhang.nozdormu.CustomObserver;
import com.cashzhang.nozdormu.FeedlyApi;
import com.cashzhang.nozdormu.FeedlyRequest;
import com.cashzhang.nozdormu.LoginActivity;
import com.cashzhang.nozdormu.Constants;
import com.cashzhang.nozdormu.CustomListener;
import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.RxUtils;
import com.cashzhang.nozdormu.Settings;
import com.cashzhang.nozdormu.model.Profile;
import com.cashzhang.nozdormu.service.UpdateService;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz21 on 2018/5/22.
 */

public class LeftFragment extends Fragment {

    private final static String TAG = LeftFragment.class.getSimpleName();

    private static String accessToken = null;
    private static String refreshToken = null;

    private Activity activity;

    @BindView(R.id.left_text)
    TextView leftText;
    @BindView(R.id.accounts_img)
    ImageView imageView;

    public static LeftFragment newInstance() {
        return new LeftFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
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
        if (requestCode == 404) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "auth success");
                accessToken = Settings.getAccessToken();
                refreshToken = Settings.getRefreshToken();
            }
        }
        final FeedlyApi feedlyApi = FeedlyRequest.getInstance();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feedly-Access-Token", accessToken);

        CustomListener<Profile> listener = new CustomListener<Profile>() {
            @Override
            public void onNext(Profile profile) {
                Log.d(TAG, "onNext: profile.email = " + profile.email);
                Log.d(TAG, "onNext: profile.FamilyName = Mr./Ms. " + profile.familyName);
                Settings.setId(profile.id);
                Settings.setEmail(profile.email);
                Settings.setGivenName(profile.givenName);
                leftText.setText(Settings.getEmail());
            }

            @Override
            public void onComplete() {
                activity.startService(new Intent(activity, UpdateService.class));
            }
        };
        RxUtils.CustomSubscribe(feedlyApi.getProfile(headers), new CustomObserver<>(listener));

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
