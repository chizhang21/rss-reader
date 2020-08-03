package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Visual : Serializable {
    @SerializedName("processor")
    @Expose
    var processor: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null

    @SerializedName("expirationDate")
    @Expose
    var expirationDate: Long? = null

    @SerializedName("edgeCacheUrl")
    @Expose
    var edgeCacheUrl: String? = null

    @SerializedName("contentType")
    @Expose
    var contentType: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("processor", processor).append("url", url).append("width", width).append("height", height).append("expirationDate", expirationDate).append("edgeCacheUrl", edgeCacheUrl).append("contentType", contentType).toString()
    }

    companion object {
        private const val serialVersionUID = 4317566651350877241L
    }
}