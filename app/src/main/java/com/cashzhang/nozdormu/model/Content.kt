package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Content : Serializable {
    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("direction")
    @Expose
    var direction: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("content", content).append("direction", direction).toString()
    }

    companion object {
        private const val serialVersionUID = -5071535977706819040L
    }
}