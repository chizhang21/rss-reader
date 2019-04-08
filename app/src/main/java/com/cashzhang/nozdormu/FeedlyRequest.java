package com.cashzhang.nozdormu;

public class FeedlyRequest{
    private static volatile FeedlyApi mfeedlyApi;
    FeedlyRequest() {
    }
    public static FeedlyApi getInstance() {
        if (mfeedlyApi == null) {
            synchronized (FeedlyRequest.class) {
                if (mfeedlyApi == null) {
                    mfeedlyApi = ServiceGenerator.createService(FeedlyApi.class);
                }
            }
        }
        return mfeedlyApi;
    }
}
