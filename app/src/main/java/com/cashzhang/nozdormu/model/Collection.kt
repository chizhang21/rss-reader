package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Collection : Serializable {
    @SerializedName("customizable")
    @Expose
    var customizable: Boolean? = null

    @SerializedName("enterprise")
    @Expose
    var enterprise: Boolean? = null

    @SerializedName("feeds")
    @Expose
    var feeds: List<Feed>? = null

    @SerializedName("label")
    @Expose
    var label: String? = null

    @SerializedName("numFeeds")
    @Expose
    var numFeeds: Int? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("customizable", customizable).append("enterprise", enterprise).append("feeds", feeds).append("label", label).append("numFeeds", numFeeds).append("id", id).toString()
    }

    companion object {
        private const val serialVersionUID = 3151212532091514L
    }
}