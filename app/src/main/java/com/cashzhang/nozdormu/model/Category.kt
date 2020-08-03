package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Category : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("label")
    @Expose
    var label: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("id", id).append("label", label).toString()
    }

    companion object {
        private const val serialVersionUID = 841372717397246658L
    }
}