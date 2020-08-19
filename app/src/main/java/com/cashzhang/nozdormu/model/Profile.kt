package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Profile(

    @SerializedName("id")
    var id: String,

    @SerializedName("client")
    var client: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("picture")
    var picture: String,

    @SerializedName("givenName")
    var givenName: String,

    @SerializedName("familyName")
    var familyName: String,

    @SerializedName("gender")
    var gender: String,
)