package com.cashzhang.nozdormu

import com.cashzhang.nozdormu.model.*
import com.cashzhang.nozdormu.model.Collection
import io.reactivex.Observable
import retrofit2.http.*

interface FeedlyApi {
    @POST("/v3/auth/token")
    fun loginWithCode(@Body loginBody: LoginBody?): Observable<Token?>

    @GET("/v3/profile")
    fun getProfile(@HeaderMap headers: Map<String?, String?>?): Observable<Profile?>?

    @GET("/v3/collections")
    fun getCollections(@HeaderMap headers: Map<String?, String?>?): Observable<List<Collection?>?>?

    @GET("/v3/streams/contents")
    fun getStreams(@Query("streamId") streamId: String?, @HeaderMap headers: Map<String?, String?>?): Observable<Streams?>?
}