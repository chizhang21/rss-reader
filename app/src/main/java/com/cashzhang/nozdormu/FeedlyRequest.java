package com.cashzhang.nozdormu;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class FeedlyRequest{

    public static final String API_BASE_URL = "https://cloud.feedly.com";
    private static volatile FeedlyApi mFeedlyApi;
    private FeedlyRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mFeedlyApi = retrofit.create(FeedlyApi.class);
    }
    public static FeedlyApi getInstance() {
        if (mFeedlyApi == null) {
            synchronized (FeedlyRequest.class) {
                if (mFeedlyApi == null) {
                    new FeedlyRequest();
                }
            }
        }
        return mFeedlyApi;
    }
}
