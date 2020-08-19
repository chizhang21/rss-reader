package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Mention(

    @SerializedName("text")
    var text: String
)