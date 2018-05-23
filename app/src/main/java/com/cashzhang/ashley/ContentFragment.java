package com.cashzhang.ashley;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangchi on 2018/3/30.
 */

public class ContentFragment extends Fragment {

    private final static String TAG = "ashley-rss";
    private String title = null;
    private String time = null;
    private String url = null;
    private String content = null;
    @BindView(R.id.c_title) TextView mTitle;
    @BindView(R.id.c_content) TextView mContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.content_page, container, false);

        ButterKnife.bind(this, layout);

        if (mTitle != null && mContent != null) {
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
                                DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                                int dwidth = dm.widthPixels - 32;
                                float dheight = (float) d.getIntrinsicHeight() * (float) dwidth / (float) d.getIntrinsicWidth();
                                int dh = (int) (dheight + 0.5);
                                int wid = dwidth;
                                int hei = dh;
                                d.setBounds(0, 0, wid, hei);
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
        Log.d(TAG, "ContentFragment onCreateView: ");
        return layout;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        time = bundle.getString("time");
        url = bundle.getString("url");
        content = bundle.getString("content");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
            case android.R.id.home:
                getActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
