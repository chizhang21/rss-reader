package com.cashzhang.ashley.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;

import com.cashzhang.ashley.Constants;
import com.cashzhang.ashley.FeedItem;
import com.cashzhang.ashley.IndexItem;
import com.cashzhang.ashley.MainActivity;
import com.cashzhang.ashley.ObjectIO;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class ServiceUpdate extends IntentService {
    public static final String FEED_BROADCAST_ACTION = "com.cashzhang.serviceupdate.handle";
    public static final String ITEM_LIST = "-item_list.txt";
    private final static String TAG = "ashley-rss";
    private String tmpTitle = "";

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

    public ServiceUpdate() {
        super("ServiceUpdate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: service update");
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

        Intent broadcast = new Intent(FEED_BROADCAST_ACTION);
        sendBroadcast(broadcast);
        stopSelf();
    }

    private void parseFeed(CharSequence urlString, long uid) throws XmlPullParserException, IOException {
        String contentFile = Long.toString(uid);
        Log.d(TAG, "parseFeed");

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
        FeedItem feedItem;

        int eventType;

        //for web title
        do {
            parser.next();
            eventType = parser.getEventType();
        }
        while ((XmlPullParser.START_TAG != eventType || !Tags.TITLE.equals(parser.getName())));

        String tagtag = parser.getName();
        feedItem = new FeedItem();
        if (tagtag.equals(Tags.TITLE)) {
            String title = getContent(parser);
            tmpTitle = title;
        }

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
                } else if (tag.equals(Tags.PUBLISHED) || tag.equals(Tags.PUB_DATE)) {
                    setPublishedTime(feedItem, getContent(parser), tag);
                } else if (tag.equals(Tags.TITLE)) {
                    String title = getContent(parser);
                    feedItem.m_title = title;
                } else if (tag.equals(Tags.CONTENT) || tag.equals(Tags.DESCRIPTION)) {
                    String content = getContent(parser);
                    feedItem.m_content = content;
                    feedItem.m_tcontent = Patterns.CDATA.matcher(content).replaceAll("").trim();
                }
                feedItem.m_webtitle = tmpTitle;
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



