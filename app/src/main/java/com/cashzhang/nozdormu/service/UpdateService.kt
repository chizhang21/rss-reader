package com.cashzhang.nozdormu.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Base64
import android.util.Log
import com.cashzhang.nozdormu.*
import com.cashzhang.nozdormu.model.Collection
import com.cashzhang.nozdormu.model.Streams
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.util.*

class UpdateService : Service() {
    var context: Context = this
    var feedIdList: ArrayList<String?>? = null
    var collectionNameList: ArrayList<String>? = null
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        feedIdList = ArrayList()
        collections
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    //                    collectionNameList.add(collectionName);
    val collections: Unit
        get() {
            val feedlyApi = FeedlyRequest.instance
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/json"
            headers["X-Feedly-Access-Token"] = Settings.accessToken
            val listener: CustomListener<List<Collection>?> = object : CustomListener<List<Collection?>?> {
                @Throws(IOException::class)
                override fun onNext(collections: List<Collection>) {
                    for (collection in collections) {
                        val collectionName = collection.label
                        for (feed in collection.feeds!!) {
                            val feedId = feed.feedId
                            val file = File(context.getExternalFilesDir("collections/$collectionName").toString() + "/" + Base64.encodeToString(feedId!!.toByteArray(charset("UTF-8")), Base64.DEFAULT))
                            Log.d(TAG, "Collections: filePath=" + file.absolutePath)
                            if (!file.exists()) file.createNewFile()
                            val out = ObjectOutputStream(FileOutputStream(file))
                            try {
                                out.writeObject(feed)
                            } finally {
                                out.close()
                            }
                            feedIdList!!.add(feedId)
                        }
                        //                    collectionNameList.add(collectionName);
                    }
                }

                override fun onComplete() {
                    for (name in feedIdList!!) {
                        getFeedStream(name)
                    }
                }
            }
            RxUtils.CustomSubscribe(feedlyApi.getCollections(headers), CustomObserver<Any?>(listener))
        }

    private fun getFeedStream(feedId: String?) {
        val feedlyApi = FeedlyRequest.instance
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        headers["X-Feedly-Access-Token"] = Settings.accessToken
        Log.d(TAG, "accessToken: " + Settings.accessToken)
        val listener: CustomListener<Streams?> = object : CustomListener<Streams?> {
            @Throws(IOException::class)
            override fun onNext(response: Streams) {
                Log.d(TAG, "onNext")
                val file = File(context.getExternalFilesDir("streams").toString() + "/" + Base64.encodeToString(feedId!!.toByteArray(charset("UTF-8")), Base64.DEFAULT))
                Log.d(TAG, "FeedStream: filePath=" + file.absolutePath)
                if (!file.exists()) file.createNewFile()
                val out = ObjectOutputStream(FileOutputStream(file))
                try {
                    out.writeObject(response)
                } finally {
                    out.close()
                }
            }

            override fun onComplete() {}
        }
        RxUtils.CustomSubscribe(feedlyApi.getStreams(feedId, headers), CustomObserver<Any?>(listener))
    } /*private boolean writeEachCollectionToFile(Collection collection) throws FileNotFoundException {
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
    companion object {
        private val TAG = UpdateService::class.java.simpleName
    }
}