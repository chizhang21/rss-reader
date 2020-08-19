package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

class Token(

    @SerializedName("access_token")
    var access_token: String,

    @SerializedName("text")
    var refresh_token: String,

    @SerializedName("text")
    var plan: String,

    @SerializedName("text")
    var expires_in: String,

    @SerializedName("text")
    var id: String,
)