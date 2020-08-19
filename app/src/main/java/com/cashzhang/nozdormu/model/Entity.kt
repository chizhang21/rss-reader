package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

class Entity(

    @SerializedName("id")
    var id: String,

    @SerializedName("salienceLevel")
    var salienceLevel: String,

    @SerializedName("label")
    var label: String,

    @SerializedName("mentions")
    var mentions: List<Mention>
)
