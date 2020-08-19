package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Collection(

    @SerializedName("customizable")
    val customizable: Boolean,

    @SerializedName("enterprise")
    val enterprise: Boolean,

    @SerializedName("feeds")
    val feeds: List<Feed>,

    @SerializedName("label")
    val label: String?,

    @SerializedName("numFeeds")
    val numFeeds: Int,

    @SerializedName("id")
    val id: String,
)
