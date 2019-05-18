package com.cashzhang.nozdormu;

import com.cashzhang.nozdormu.bean.Feed;
import com.cashzhang.nozdormu.bean.Collection;
import com.cashzhang.nozdormu.bean.LoginBody;
import com.cashzhang.nozdormu.bean.Profile;
import com.cashzhang.nozdormu.bean.Streams;
import com.cashzhang.nozdormu.bean.Token;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedlyApi {

    @POST("/v3/auth/token")
    Observable<Token> loginWithCode(@Body LoginBody loginBody);

    @GET("/v3/profile")
    Observable<Profile> getProfile(@HeaderMap Map<String, String> headers);

    @GET("/v3/collections")
    Observable<List<Collection>> getCollections(@HeaderMap Map<String, String> headers);

    @GET("/v3/streams/{streamId}/ids")
    Observable<Streams> getStreams(@Path("streamId") String streamId, @HeaderMap Map<String, String> headers);

}
