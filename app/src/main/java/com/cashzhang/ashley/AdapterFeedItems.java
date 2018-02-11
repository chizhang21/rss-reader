package com.cashzhang.ashley;

import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

/**
 * Created by zhangchi on 2018/2/11.
 */

public class AdapterFeedItems extends LinkedMapAdapter<Long, FeedItem> {
    private final MainActivity m_activity;

    public AdapterFeedItems(MainActivity activity, Map<Long, FeedItem> map) {
        super(map);
        m_activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Type type = Type.values()[getItemViewType(position)];
        FeedItem item = getItem(position);
        boolean recycled = null != convertView;
        boolean hasImage = Type.IMAGE == type || Type.IMAGE_SANS_DESCRIPTION == type;

        ViewFeedItem view = recycled ? (ViewFeedItem) convertView : new ViewFeedItem(m_activity, type);

        boolean isRead = false; /*m_activity.isItemRead(item.m_time);*/
        boolean shouldInvalidate = view.m_isViewRead != isRead;


        // If the recycled view is the view we want, keep it.
        if (recycled && item.m_time.equals(view.m_item.m_time)) {
            if (shouldInvalidate) {
                view.invalidate();
            }
            return view;
        }

        // Set the information.
        view.m_item = item;

        // If the view was an image, load the image.
        if (hasImage) {
            view.setBitmap(null);
            view.setTag(item.m_time);
            AsyncLoadImage.newInstance(view, item.m_imageName, item.m_time);
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        FeedItem item = getItem(position);

        boolean isDes = !item.m_desLines[0].isEmpty();

        if (item.m_imageLink.isEmpty()) {
            return isDes ? Type.PLAIN.ordinal() : Type.PLAIN_SANS_DESCRIPTION.ordinal();
        } else {
            return isDes ? Type.IMAGE.ordinal() : Type.IMAGE_SANS_DESCRIPTION.ordinal();
        }
    }

    @Override
    public int getViewTypeCount() {
        return Type.values().length;
    }

    public enum Type {
        PLAIN,
        IMAGE,
        IMAGE_SANS_DESCRIPTION,
        PLAIN_SANS_DESCRIPTION,
    }
}
