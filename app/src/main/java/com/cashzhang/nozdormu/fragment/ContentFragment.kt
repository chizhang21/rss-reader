package com.cashzhang.nozdormu.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.cashzhang.nozdormu.MainActivity
import com.cashzhang.nozdormu.R
import com.cashzhang.nozdormu.model.Item
import kotlin.Throws

/**
 * Created by cz21 on 2018/3/30.
 */
class ContentFragment : Fragment() {
    private var title: String? = null
    private var time: String? = null
    private var url: String? = null
    private var content: String? = null
    private var item: Item? = null
    var bundle: Bundle? = null

    @BindView(R.id.c_title)
    var mTitle: TextView? = null

    @BindView(R.id.content_web)
    var webView: WebView? = null
    var activity: Activity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "ContentFragment onCreateView: ")
        super.onCreateView(inflater, container, savedInstanceState)
        val layout = inflater.inflate(R.layout.content_page, container, false)
        ButterKnife.bind(this, layout)
        return layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "ContentFragment onCreate: ")
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG, "setUserVisibleHint() -> isVisibleToUser: $isVisibleToUser")
        if (isVisibleToUser) {
            loadData(MainActivity.bundle)
        }
    }

    fun loadData(tmpBundle: Bundle?) {
        try {
            bundle = tmpBundle
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (bundle != null) {
            item = bundle!!.getSerializable("item_id") as Item
            title = item!!.title
            time = item!!.published.toString()
            url = item!!.originId
            content = if (item!!.content != null) item!!.content.content else item!!.summary.content
        }
        if (mTitle != null && webView != null && title != null) {
            mTitle!!.text = title
            webView!!.settings.javaScriptEnabled = true
            webView!!.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {}
            }
            webView!!.settings.setSupportZoom(false)
            webView!!.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
            webView!!.settings.userAgentString = "Chrome/56.0.0.0 Mobile"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "ContentFragment onCreateOptionsMenu: ")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.content_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_brower -> {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                true
            }
            R.id.mercury -> {
                Log.d(TAG, "origin ID=$url")
                //                mercuryHttp();
                true
            }
            android.R.id.home -> {
                getActivity()!!.onBackPressed()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    } //    private void mercuryHttp() {

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
    companion object {
        private val TAG = ContentFragment::class.java.simpleName
        fun newInstance(): ContentFragment {
            return ContentFragment()
        }
    }
}