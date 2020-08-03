package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Origin : Serializable {
    @SerializedName("streamId")
    @Expose
    var streamId: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("htmlUrl")
    @Expose
    var htmlUrl: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("streamId", streamId).append("title", title).append("htmlUrl", htmlUrl).toString()
    }

    companion object {
        private const val serialVersionUID = -174037031250207312L
    }
}