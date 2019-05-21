package com.cashzhang.nozdormu.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cashzhang.nozdormu.Constants;
import com.cashzhang.nozdormu.CustomListener;
import com.cashzhang.nozdormu.CustomObserver;
import com.cashzhang.nozdormu.FeedItem;
import com.cashzhang.nozdormu.FeedlyApi;
import com.cashzhang.nozdormu.FeedlyRequest;
import com.cashzhang.nozdormu.ObjectIO;
import com.cashzhang.nozdormu.RxUtils;
import com.cashzhang.nozdormu.Settings;
import com.cashzhang.nozdormu.bean.Collection;
import com.cashzhang.nozdormu.bean.Feed;
import com.cashzhang.nozdormu.bean.Item;
import com.cashzhang.nozdormu.bean.Streams;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class UpdateService extends Service {
    private final static String TAG = UpdateService.class.getSimpleName();
    Context m_context = this;
    ArrayList<String> feedIdList;
    ArrayList<String> collectionNameList;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        feedIdList = new ArrayList<>();
        getCollections();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void getCollections() {
        FeedlyApi feedlyApi = FeedlyRequest.getInstance();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feedly-Access-Token", Settings.getAccessToken());
        CustomListener<List<Collection>> listener = new CustomListener<List<Collection>>() {
            @Override
            public void onNext(List<Collection> collections) throws IOException {
                for (Collection collection : collections) {
                    String collectionName = collection.getLabel();
                    for (Feed feed : collection.getFeeds()) {
                        String feedId = feed.getFeedId();
                        File file = new File(m_context.getExternalFilesDir("collections/"+collectionName)+"/"+Base64.encodeToString(feedId.getBytes("UTF-8"), Base64.DEFAULT));
                        Log.d(TAG, "Collections: filePath="+file.getAbsolutePath());
                        if (!file.exists())
                            file.createNewFile();
                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                        try {
                            out.writeObject(feed);
                        } finally {
                            out.close();
                        }
                        feedIdList.add(feedId);
                    }
//                    collectionNameList.add(collectionName);
                }
            }
            @Override
            public void onComplete() {
                for (String name : feedIdList) {
                    getFeedStream(name);
                }
            }
        };
        RxUtils.CustomSubscribe(feedlyApi.getCollections(headers), new CustomObserver(listener));
    }

    private void getFeedStream(final String feedId) {
        FeedlyApi feedlyApi = FeedlyRequest.getInstance();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Feedly-Access-Token", Settings.getAccessToken());
        Log.d(TAG, "accessToken: " + Settings.getAccessToken());
        CustomListener<Streams> listener = new CustomListener<Streams>() {
            @Override
            public void onNext(Streams response) throws IOException {
                Log.d(TAG, "onNext");
                File file = new File(m_context.getExternalFilesDir("streams")+"/"+Base64.encodeToString(feedId.getBytes("UTF-8"), Base64.DEFAULT));
                Log.d(TAG, "FeedStream: filePath="+file.getAbsolutePath());
                if (!file.exists())
                    file.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                try {
                    out.writeObject(response);
                } finally {
                    out.close();
                }

            }

            @Override
            public void onComplete() {

            }
        };
        RxUtils.CustomSubscribe(feedlyApi.getStreams(feedId, headers), new CustomObserver(listener));
    }

    /*private boolean writeEachCollectionToFile(Collection collection) throws FileNotFoundException {
        if (collection == null)
            return false;
        objectIO.setNewFileName(collection.getLabel());
        return objectIO.write(collection);
    }*/
    /*private void parseFeed(CharSequence urlString, long uid) throws XmlPullParserException, IOException {
        String contentFile = Long.toString(uid);
        Log.d(TAG, "parseFeed");

        if (new File(getFilesDir(), contentFile + "-content.txt").exists()) {
            ObjectIO reader = new ObjectIO(this, uid + "-content.txt",2);
            Map<Long, FeedItem> tempMap = (Map<Long, FeedItem>) reader.read();
            deleteFile(uid + "-content.txt");

            ObjectIO writer = new ObjectIO(this, Long.toString(uid),2);
            writer.write(tempMap);
        }

        String longFile = uid + ITEM_LIST;

        Map<Long, FeedItem> map = new TreeMap<Long, FeedItem>(Collections.reverseOrder());

        ObjectIO reader = new ObjectIO(this, contentFile,2);
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
        ObjectIO out = new ObjectIO(this, contentFile,2);
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
    }*/
}



