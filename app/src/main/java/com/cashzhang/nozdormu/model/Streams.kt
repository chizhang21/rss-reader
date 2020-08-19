package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Streams(

    @SerializedName("id")
    var id: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("updated")
    var updated: Long,

    @SerializedName("alternate")
    var alternate: List<Alternate>,

    @SerializedName("direction")
    var direction: String,

    @SerializedName("continuation")
    var continuation: String,

    @SerializedName("items")
    var items: List<Item>,
)