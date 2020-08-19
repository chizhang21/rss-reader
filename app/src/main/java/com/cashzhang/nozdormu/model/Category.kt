package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Category(

    @SerializedName("id")
    var id: String,

    @SerializedName("label")
    var label: String,
)
