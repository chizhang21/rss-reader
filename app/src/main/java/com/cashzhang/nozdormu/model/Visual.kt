package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Visual(

    @SerializedName("processor")
    var processor: String,

    @SerializedName("url")
    var url: String,

    @SerializedName("width")
    var width: Int,

    @SerializedName("height")
    var height: Int,

    @SerializedName("expirationDate")
    var expirationDate: Long,

    @SerializedName("edgeCacheUrl")
    var edgeCacheUrl: String,

    @SerializedName("contentType")
    var contentType: String,
)