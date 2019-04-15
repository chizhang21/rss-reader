package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashzhang.nozdormu.Constants;
import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.bean.Mercury;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz21 on 2018/3/30.
 */

public class ContentFragment extends Fragment {

    private final static String TAG = "nozdormu";
    private String title = null;
    private String time = null;
    private String url = null;
    private String content = null;

    Bundle bundle;
    @BindView(R.id.c_title)
    TextView mTitle;
    @BindView(R.id.c_content)
    TextView mContent;

    Activity activity;

    public static ContentFragment newInstance() {
        ContentFragment contentFragment = new ContentFragment();
        return contentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "ContentFragment onCreateView: ");
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.content_page, container, false);
        ButterKnife.bind(this, layout);

        return layout;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "ContentFragment onCreate: ");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint() -> isVisibleToUser: " + isVisibleToUser);
        if (isVisibleToUser)
            loadData(MainActivity.getBundle());
    }

    public void loadData(Bundle tmpBundle) {
        try {
            bundle = tmpBundle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            title = bundle.getString("title");
            time = bundle.getString("time");
            url = bundle.getString("url");
            content = bundle.getString("content");
        }

        if (mTitle != null && mContent != null && title != null) {
            mTitle.setText(title);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Spanned sp = Html.fromHtml(content, new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            InputStream is = null;
                            try {
                                is = (InputStream) new URL(source).getContent();

                                Drawable d = Drawable.createFromStream(is, "src");
                                if (d == null)
                                    d = Drawable.createFromStream(is, "href");
                                DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                                int dwidth = dm.widthPixels - 50;

                                float dheight = (float) d.getIntrinsicHeight() * (float) dwidth / (float) d.getIntrinsicWidth();
                                int dh = (int) (dheight - 0.5);
                                int wid = dwidth;
                                int hei = dh;
                                //fix blank line bewteen text and image
                                int radio = (int) (hei * 0.25);
                                d.setBounds(0, 0 - radio, wid, hei - radio);

                                is.close();
                                return d;
                            } catch (Exception e) {
                                return null;
                            }
                        }
                    }, null);
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mContent.setText(sp);
                                mContent.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "ContentFragment onCreateOptionsMenu: ");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.content_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_brower:
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            case R.id.mercury:
                Log.d(TAG, "origin ID="+url);
                mercuryHttp();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void mercuryHttp() {
        //success listener
        final Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                final Mercury mercury = JSON.parseObject(response, Mercury.class);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Spanned sp = Html.fromHtml(mercury.getContent(), new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                InputStream is = null;
                                try {
                                    is = (InputStream) new URL(source).getContent();

                                    Drawable d = Drawable.createFromStream(is, "src");
                                    if (d == null)
                                        d = Drawable.createFromStream(is, "href");
                                    DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                                    int dwidth = dm.widthPixels - 50;

                                    float dheight = (float) d.getIntrinsicHeight() * (float) dwidth / (float) d.getIntrinsicWidth();
                                    int dh = (int) (dheight - 0.5);
                                    int wid = dwidth;
                                    int hei = dh;
                                    //fix blank line bewteen text and image
                                    int radio = (int) (hei * 0.25);
                                    d.setBounds(0, 0 - radio, wid, hei - radio);

                                    is.close();
                                    return d;
                                } catch (Exception e) {
                                    return null;
                                }
                            }
                        }, null);
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mContent.setText(sp);
                                    mContent.setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

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
        StringRequest stringRequest = new StringRequest("https://mercury.postlight.com/parser?url="+url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-key", "d465HuY2EIvXwPiFSFbrauq6iBBCLnT9hje40yPi");
                return headers;
            }
        };
        VolleyController.getInstance(Constants.s_activity).addToRequestQueue(stringRequest);
    }
}

