package com.cashzhang.nozdormu.network

import com.cashzhang.nozdormu.model.*
import com.cashzhang.nozdormu.model.Collection
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FeedlyService {

    @POST("/v3/auth/token")
    fun loginWithCode(
        @Body loginBody: LoginBody
    ): Single<Token>

    @GET("/v3/profile")
    fun getProfile(
        @HeaderMap headers: Map<String, String?>
    ): Single<Profile>

    @GET("/v3/collections")
    fun getCollections(
        @HeaderMap headers: Map<String, String?>
    ): Single<List<Collection>>

    @GET("/v3/streams/contents")
    fun getStreams(
        @Query("streamId") streamId: String,
        @HeaderMap headers: Map<String, String?>
    ): Single<Streams>

    companion object {
        private const val FEEDLY_BASE_URL = "https://cloud.feedly.com"
        val INSTANCE: FeedlyService = Retrofit.Builder()
            .baseUrl(FEEDLY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(FeedlyService::class.java)
    }
}