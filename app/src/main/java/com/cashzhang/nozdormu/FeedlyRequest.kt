package com.cashzhang.nozdormu

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FeedlyRequest private constructor() {
    companion object {
        const val API_BASE_URL = "https://cloud.feedly.com"

        @Volatile
        private var mFeedlyApi: FeedlyApi?
        @JvmStatic
        val instance: FeedlyApi?
            get() {
                if (mFeedlyApi == null) {
                    synchronized(FeedlyRequest::class.java) {
                        if (mFeedlyApi == null) {
                            FeedlyRequest()
                        }
                    }
                }
                return mFeedlyApi
            }
    }

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        mFeedlyApi = retrofit.create(FeedlyApi::class.java)
    }
}