package com.cashzhang.ashley;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUpdate extends IntentService {
    public static final String BROADCAST_ACTION = "com.cashzhang.serviceupdate.handle";
    public static final String ITEM_LIST = "-item_list.txt";
    private final static String TAG = "ashley-rss";

    private static class Tags {
        static final String LINK = "link";
        static final String PUBLISHED = "published";
        static final String PUB_DATE = "pubDate";
        static final String TITLE = "title";
        static final String DESCRIPTION = "description";
        static final String CONTENT = "content";
        static final String ENTRY = "entry";
        static final String ITEM = "item";
    }

    private static class Patterns {
        static final Pattern CDATA = Pattern.compile("\\<.*?\\>");
    }

    private static final String NEWLINE = System.getProperty("line.separator");
    private static final float FAKE_WIDTH = Math.min(Resources.getSystem()
            .getDisplayMetrics().widthPixels, Resources.getSystem()
            .getDisplayMetrics().heightPixels);
    public ServiceUpdate() {
        super("ServiceUpdate");
    }
    public ServiceUpdate(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ObjectIO reader = new ObjectIO(this, MainActivity.INDEX);
        Iterable<IndexItem> indexItems = (Iterable<IndexItem>) reader.read();

        if (indexItems != null) {
            // Download and parse each feed in the index.
            for (IndexItem indexItem : indexItems) {
                try {
                    parseFeed(indexItem.m_url, indexItem.m_uid);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }

        Intent broadcast = new Intent(BROADCAST_ACTION);
        sendBroadcast(broadcast);
        stopSelf();
    }

    private void parseFeed(CharSequence urlString, long uid) throws XmlPullParserException, IOException {
        String contentFile = Long.toString(uid);

        if (new File(getFilesDir(), contentFile + "-content.txt").exists()) {
            ObjectIO reader = new ObjectIO(this, uid + "-content.txt");
            Map<Long, FeedItem> tempMap = (Map<Long, FeedItem>) reader.read();
            deleteFile(uid + "-content.txt");

            ObjectIO writer = new ObjectIO(this, Long.toString(uid));
            writer.write(tempMap);
        }

        String longFile = uid + ITEM_LIST;

        Map<Long, FeedItem> map = new TreeMap<Long, FeedItem>(Collections.reverseOrder());

        ObjectIO reader = new ObjectIO(this, contentFile);
        Map<Long, FeedItem> mapFromFile = (Map<Long, FeedItem>) reader.read();

        if (null != mapFromFile) {
            map.putAll(mapFromFile);
        }

        XmlPullParser parser = Constants.createXmlParser(urlString);
        FeedItem feedItem = new FeedItem();

        int eventType;
        do {
            parser.next();
            eventType = parser.getEventType();
        }
        while ((XmlPullParser.START_TAG != eventType || !Tags.ENTRY.equals(parser.getName())) &&
                (XmlPullParser.START_TAG != eventType || !Tags.ITEM.equals(parser.getName())) &&
                XmlPullParser.END_DOCUMENT != eventType);
        while (XmlPullParser.END_DOCUMENT != eventType) {
            if (XmlPullParser.START_TAG == eventType) {
                String tag = parser.getName();

                if (tag.equals(Tags.ENTRY) || tag.equals(Tags.ITEM)) {
                    feedItem = new FeedItem();
                } else if (tag.equals(Tags.LINK)) {
                    String link = parser.getAttributeValue(null, "href");
                    if (null == link) {
                        link = getContent(parser);
                    }
                    feedItem.m_url = link;
                    Log.d(TAG, "parseFeed url: " + feedItem.m_url);
                } else if (tag.equals(Tags.PUBLISHED) || tag.equals(Tags.PUB_DATE)) {
                    setPublishedTime(feedItem, getContent(parser), tag);
                } else if (tag.equals(Tags.TITLE)) {
                    String title = getContent(parser);
                    feedItem.m_title = title;
                    feedItem.m_webtitle = title;//TODO tmp
                } else if (tag.equals(Tags.CONTENT) || tag.equals(Tags.DESCRIPTION)) {
                    String content = getContent(parser);
                    content = Patterns.CDATA.matcher(content).replaceAll("").trim();
                    feedItem.m_content = content;
                }
            } else if (XmlPullParser.END_TAG == eventType) {
                String tag = parser.getName();
                boolean newItem = !map.containsKey(feedItem.m_time);

                if (Tags.ENTRY.equals(tag) || Tags.ITEM.equals(tag) && newItem) {
                    map.put(feedItem.m_time, feedItem);
                }
            }
            parser.next();
            eventType = parser.getEventType();
        }

        // Write the map to file.
        ObjectIO out = new ObjectIO(this, contentFile);
        out.write(map);
        Log.d(TAG, "parseFeed: out.write(map)");

        // Write the key set to file.
        Set<Long> set = new HashSet<Long>(map.keySet());
        out.setNewFileName(longFile);
        out.write(set);
    }

    private static String getContent(XmlPullParser parser) {
        try {
            parser.next();
        } catch (XmlPullParserException ignored) {
            return "";
        } catch (IOException ignored) {
            return "";
        }
        String content = parser.getText();
        return null == content ? "" : content;
    }

    private static void setPublishedTime(FeedItem feedItem, String content, String tag) {
        Time time = new Time();
        try {
            // <published> - It is an atom feed it will be one of four RFC3339 formats.
            if (Tags.PUBLISHED.equals(tag)) {
                time.parse3339(content);
                feedItem.m_time = time.toMillis(true);
            }
            // <pubDate> - It follows the rss 2.0 specification for rfc882.
            else {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat rssDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                Date date = rssDate.parse(content);
                calendar.setTime(date);
                feedItem.m_time = calendar.getTimeInMillis();
            }
        } catch (ParseException ignored) {
            time.setToNow();
            feedItem.m_time = time.toMillis(true);
        } catch (RuntimeException ignored) {
            time.setToNow();
            feedItem.m_time = time.toMillis(true);
        }
    }
}



