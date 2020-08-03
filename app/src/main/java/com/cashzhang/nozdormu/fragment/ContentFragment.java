package com.cashzhang.nozdormu.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cashzhang.nozdormu.MainActivity;
import com.cashzhang.nozdormu.R;
import com.cashzhang.nozdormu.model.Item;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cz21 on 2018/3/30.
 */

public class ContentFragment extends Fragment {

    private final static String TAG = ContentFragment.class.getSimpleName();
    private String title = null;
    private String time = null;
    private String url = null;
    private String content = null;
    private Item item;

    Bundle bundle;
    @BindView(R.id.c_title)
    TextView mTitle;
    @BindView(R.id.content_web)
    WebView webView;

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
        if (isVisibleToUser) {
            loadData(MainActivity.bundle);
        }
    }

    public void loadData(Bundle tmpBundle) {
        try {
            bundle = tmpBundle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            item = (Item) bundle.getSerializable("item_id");
            title = item.title;
            time = item.published.toString();
            url = item.originId;
            if (item.content != null)
                content = item.content.content;
            else
                content = item.summary.content;
        }

        if (mTitle != null && webView != null && title != null) {
            mTitle.setText(title);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {

                }
            });
            webView.getSettings().setSupportZoom(false);
            webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
            webView.getSettings().setUserAgentString("Chrome/56.0.0.0 Mobile");
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
//                mercuryHttp();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    private void mercuryHttp() {
//        //success listener
//        final Response.Listener listener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, response);
//                final Mercury mercury = JSON.parseObject(response, Mercury.class);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final Spanned sp = Html.fromHtml(mercury.getContent(), new Html.ImageGetter() {
//                            @Override
//                            public Drawable getDrawable(String source) {
//                                InputStream is = null;
//                                try {
//                                    is = (InputStream) new URL(source).getContent();
//
//                                    Drawable d = Drawable.createFromStream(is, "src");
//                                    if (d == null)
//                                        d = Drawable.createFromStream(is, "href");
//                                    DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
//                                    int dwidth = dm.widthPixels - 50;
//
//                                    float dheight = (float) d.getIntrinsicHeight() * (float) dwidth / (float) d.getIntrinsicWidth();
//                                    int dh = (int) (dheight - 0.5);
//                                    int wid = dwidth;
//                                    int hei = dh;
//                                    //fix blank line bewteen text and image
//                                    int radio = (int) (hei * 0.25);
//                                    d.setBounds(0, 0 - radio, wid, hei - radio);
//
//                                    is.close();
//                                    return d;
//                                } catch (Exception e) {
//                                    return null;
//                                }
//                            }
//                        }, null);
//                        try {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mContent.setText(sp);
//                                    mContent.setMovementMethod(LinkMovementMethod.getInstance());
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//
//            }
//        };
//        //error listener
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.getMessage(), error);
//            }
//        };
//        //GET request
//        StringRequest stringRequest = new StringRequest("https://mercury.postlight.com/parser?url="+url,
//                listener, errorListener) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("x-api-key", "d465HuY2EIvXwPiFSFbrauq6iBBCLnT9hje40yPi");
//                return headers;
//            }
//        };
//        VolleyController.getInstance(Constants.s_activity).addToRequestQueue(stringRequest);
//    }
}

