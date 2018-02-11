package com.cashzhang.ashley;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by zhangchi on 2018/2/11.
 */

public class AsyncLoadImage extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ViewFeedItem> m_view;
    private final long m_viewTag;
    private final Context m_context;

    private AsyncLoadImage(ViewFeedItem view, long viewTag) {
        m_context = view.getContext();
        m_view = new WeakReference<ViewFeedItem>(view);
        m_viewTag = viewTag;
    }

    public static void newInstance(ViewFeedItem view, String imageName, long viewTag) {
        AsyncTask<String, Void, Bitmap> task = new AsyncLoadImage(view, viewTag);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageName);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        View viewReference = m_view.get();
        if (null == viewReference) {
            cancel(true);
            return null;
        }

        try {
            FileInputStream in = m_context.openFileInput(params[0]);
            try {
                return BitmapFactory.decodeStream(in);
            } finally {
                if (null != in) {
                    in.close();
                }
            }
        } catch (FileNotFoundException ignored) {
            return null;
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (isCancelled()) {
            return;
        }

        ViewFeedItem view = m_view.get();
        if (null != view) {
            Object viewTag = view.getTag();
            if (viewTag.equals(m_viewTag) && null != result) {
                view.setBitmap(result);
            }
        }
    }
}

