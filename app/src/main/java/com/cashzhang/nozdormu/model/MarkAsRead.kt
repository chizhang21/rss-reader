package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class MarkAsRead(

    @SerializedName("action")
    var action: String,

    @SerializedName("type")
    var type: String,

    @SerializedName("entryIds")
    var entryIds: List<String>,
)