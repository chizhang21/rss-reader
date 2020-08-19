package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName

data class Item(

    @SerializedName("id")
    var id: String,

    @SerializedName("keywords")
    var keywords: List<String>,

    @SerializedName("originId")
    var originId: String,

    @SerializedName("fingerprint")
    var fingerprint: String,

    @SerializedName("content")
    var content: Content,

    @SerializedName("title")
    var title: String,

    @SerializedName("author")
    var author: String,

    @SerializedName("summary")
    var summary: Summary,

    @SerializedName("alternate")
    var alternate: List<Alternate>,

    @SerializedName("crawled")
    var crawled: Long,

    @SerializedName("published")
    var published: Long,

    @SerializedName("origin")
    var origin: Origin,

    @SerializedName("visual")
    var visual: Visual,

    @SerializedName("canonicalUrl")
    var canonicalUrl: String,

    @SerializedName("unread")
    var unread: Boolean,

    @SerializedName("categories")
    var categories: List<Category>,
)
