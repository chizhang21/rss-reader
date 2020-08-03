package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Item : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("keywords")
    @Expose
    var keywords: List<String>? = null

    @SerializedName("originId")
    @Expose
    var originId: String? = null

    @SerializedName("fingerprint")
    @Expose
    var fingerprint: String? = null

    @SerializedName("content")
    @Expose
    var content: Content? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("summary")
    @Expose
    var summary: Summary? = null

    @SerializedName("alternate")
    @Expose
    var alternate: List<Alternate_>? = null

    @SerializedName("crawled")
    @Expose
    var crawled: Long? = null

    @SerializedName("published")
    @Expose
    var published: Long? = null

    @SerializedName("origin")
    @Expose
    var origin: Origin? = null

    @SerializedName("visual")
    @Expose
    var visual: Visual? = null

    @SerializedName("canonicalUrl")
    @Expose
    var canonicalUrl: String? = null

    @SerializedName("unread")
    @Expose
    var unread: Boolean? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("id", id).append("keywords", keywords).append("originId", originId).append("fingerprint", fingerprint).append("content", content).append("title", title).append("author", author).append("summary", summary).append("alternate", alternate).append("crawled", crawled).append("published", published).append("origin", origin).append("visual", visual).append("canonicalUrl", canonicalUrl).append("unread", unread).append("categories", categories).toString()
    }

    companion object {
        private const val serialVersionUID = -5040485178574463095L
    }
}