package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Content(

    @SerializedName("content")
    var content: String,

    @SerializedName("direction")
    var direction: String,
)