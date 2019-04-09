package com.cashzhang.nozdormu;

import com.cashzhang.nozdormu.bean.CategItem;
import com.cashzhang.nozdormu.bean.LoginBody;
import com.cashzhang.nozdormu.bean.Profile;
import com.cashzhang.nozdormu.bean.Token;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FeedlyApi {

    @POST("/v3/auth/token")
    Call<Token> loginWithCode(@Body LoginBody loginBody);

    @GET("/v3/profile")
    Call<Profile> getProfile(@HeaderMap Map<String, String> headers);

    @GET("/v3/subscriptions")
    Call<List<CategItem>> getSubs(@HeaderMap Map<String, String> headers);


}
