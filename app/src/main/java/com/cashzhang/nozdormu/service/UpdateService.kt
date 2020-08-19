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
import com.cashzhang.nozdormu.network.FeedlyService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.util.*

class UpdateService : Service() {
    var context: Context = this
    var feedIdList: ArrayList<String>? = null
    var collectionNameList: ArrayList<String>? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        feedIdList = ArrayList()
        collections
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //                    collectionNameList.add(collectionName);
    val collections: Unit
        get() {
            val headers = mutableMapOf<String, String?>()
            headers["Content-Type"] = "application/json"
            headers["X-Feedly-Access-Token"] = Settings.accessToken

            FeedlyService.INSTANCE.getCollections(headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { collections ->
                        for (collection in collections) {
                            val collectionName = collection.label
                            for (feed in collection.feeds!!) {
                                val feedId = feed.feedId
                                val file = File(context.getExternalFilesDir("collections/$collectionName").toString() + "/" + Base64.encodeToString(feedId!!.toByteArray(charset("UTF-8")), Base64.DEFAULT))
                                Log.d("zhangchi", "Collections: filePath=" + file.absolutePath)
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

                        for (name in feedIdList!!) {
                            getFeedStream(name)
                        }
                    },
                    onError = {

                    }
                )
        }

    private fun getFeedStream(feedId: String) {
        val headers = mutableMapOf<String, String?>()
        headers["Content-Type"] = "application/json"
        headers["X-Feedly-Access-Token"] = Settings.accessToken
        Log.d("zhangchi", "accessToken: " + Settings.accessToken)

        FeedlyService.INSTANCE.getStreams(feedId, headers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { streams ->
                    Log.d("zhangchi", "onNext")
                    val file = File(context.getExternalFilesDir("streams").toString() + "/" + Base64.encodeToString(feedId!!.toByteArray(charset("UTF-8")), Base64.DEFAULT))
                    Log.d("zhangchi", "FeedStream: filePath=" + file.absolutePath)
                    if (!file.exists()) file.createNewFile()
                    val out = ObjectOutputStream(FileOutputStream(file))
                    out.use { out ->
                        out.writeObject(streams)
                    }
                },
                onError = {

                }
            )
    }
}