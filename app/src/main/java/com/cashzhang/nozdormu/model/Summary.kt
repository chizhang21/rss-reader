package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

class Summary(

    @SerializedName("content")
    var content: String,

    @SerializedName("direction")
    var direction: String,
)