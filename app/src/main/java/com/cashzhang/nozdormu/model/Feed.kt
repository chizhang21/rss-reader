package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Feed : Serializable {
    @SerializedName("feedId")
    @Expose
    var feedId: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("updated")
    @Expose
    var updated: Long? = null

    @SerializedName("velocity")
    @Expose
    var velocity: Double? = null

    @SerializedName("topics")
    @Expose
    var topics: List<String>? = null

    @SerializedName("subscribers")
    @Expose
    var subscribers: Int? = null

    @SerializedName("website")
    @Expose
    var website: String? = null

    @SerializedName("partial")
    @Expose
    var partial: Boolean? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("contentType")
    @Expose
    var contentType: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("estimatedEngagement")
    @Expose
    var estimatedEngagement: Int? = null

    @SerializedName("iconUrl")
    @Expose
    var iconUrl: String? = null

    @SerializedName("visualUrl")
    @Expose
    var visualUrl: String? = null

    @SerializedName("coverUrl")
    @Expose
    var coverUrl: String? = null

    @SerializedName("coverColor")
    @Expose
    var coverColor: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("feedId", feedId).append("id", id).append("title", title).append("updated", updated).append("velocity", velocity).append("topics", topics).append("subscribers", subscribers).append("website", website).append("partial", partial).append("language", language).append("state", state).append("contentType", contentType).append("description", description).append("estimatedEngagement", estimatedEngagement).append("iconUrl", iconUrl).append("visualUrl", visualUrl).append("coverUrl", coverUrl).append("coverColor", coverColor).toString()
    }

    companion object {
        private const val serialVersionUID = 6554L
    }
}