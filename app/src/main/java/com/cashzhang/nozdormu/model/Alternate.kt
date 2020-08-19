package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Alternate(

    @SerializedName("href")
    var href: String,

    @SerializedName("type")
    var type: String,
)