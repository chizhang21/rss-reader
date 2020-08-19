package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Origin(

    @SerializedName("streamId")
    var streamId: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("htmlUrl")
    var htmlUrl: String,
)