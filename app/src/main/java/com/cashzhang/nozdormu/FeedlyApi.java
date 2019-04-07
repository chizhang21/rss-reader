package com.cashzhang.nozdormu;

import com.cashzhang.nozdormu.bean.LoginBody;
import com.cashzhang.nozdormu.bean.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedlyApi {

    @POST("/v3/auth/token")
    Call<Token> LoginWithCode(@Body LoginBody loginBody);


}
