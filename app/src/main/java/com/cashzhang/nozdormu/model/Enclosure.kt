package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Enclosure(

    @SerializedName("href")
    var href: String,

    @SerializedName("type")
    var type: String,

    @SerializedName("length")
    var length: Int
)
