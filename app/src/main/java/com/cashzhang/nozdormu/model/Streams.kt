package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Streams : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("updated")
    @Expose
    var updated: Long? = null

    @SerializedName("alternate")
    @Expose
    var alternate: List<Alternate>? = null

    @SerializedName("direction")
    @Expose
    var direction: String? = null

    @SerializedName("continuation")
    @Expose
    var continuation: String? = null

    @SerializedName("items")
    @Expose
    var items: List<Item>? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("id", id).append("title", title).append("updated", updated).append("alternate", alternate).append("direction", direction).append("continuation", continuation).append("items", items).toString()
    }

    companion object {
        private const val serialVersionUID = -381602297131992324L
    }
}