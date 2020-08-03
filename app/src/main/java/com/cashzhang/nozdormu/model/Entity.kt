package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder

class Entity {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("salienceLevel")
    @Expose
    var salienceLevel: String? = null

    @SerializedName("label")
    @Expose
    var label: String? = null

    @SerializedName("mentions")
    @Expose
    var mentions: List<Mention>? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("id", id).append("salienceLevel", salienceLevel).append("label", label).append("mentions", mentions).toString()
    }
}