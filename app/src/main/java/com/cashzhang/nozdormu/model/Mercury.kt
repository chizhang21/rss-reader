package com.cashzhang.nozdormu.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Mercury(

    @SerializedName("title")
    var title: String,

    @SerializedName("content")
    var content: String,

    @SerializedName("date_published")
    var date_published: Date,

    @SerializedName("lead_image_url")
    var lead_image_url: String,

    @SerializedName("dek")
    var dek: String,

    @SerializedName("url")
    var url: String,

    @SerializedName("domain")
    var domain: String,

    @SerializedName("excerpt")
    var excerpt: String,

    @SerializedName("word_count")
    var word_count: Int,

    @SerializedName("direction")
    var direction: String,

    @SerializedName("total_pages")
    var total_pages: Int,

    @SerializedName("rendered_pages")
    var rendered_pages: Int,

    @SerializedName("next_page_url")
    var next_page_url: String,
)