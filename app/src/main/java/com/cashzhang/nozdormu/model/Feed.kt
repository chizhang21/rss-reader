package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Feed(

    @SerializedName("feedId")
    var feedId: String,

    @SerializedName("id")
    var id: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("updated")
    var updated: Long,

    @SerializedName("velocity")
    var velocity: Double,

    @SerializedName("topics")
    var topics: List<String>,

    @SerializedName("subscribers")
    var subscribers: Int,

    @SerializedName("website")
    var website: String,

    @SerializedName("partial")
    var partial: Boolean,

    @SerializedName("language")
    var language: String,

    @SerializedName("state")
    var state: String,

    @SerializedName("contentType")
    var contentType: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("estimatedEngagement")
    var estimatedEngagement: Int,

    @SerializedName("iconUrl")
    var iconUrl: String,

    @SerializedName("visualUrl")
    var visualUrl: String,

    @SerializedName("coverUrl")
    var coverUrl: String,

    @SerializedName("coverColor")
    var coverColor: String,
)
