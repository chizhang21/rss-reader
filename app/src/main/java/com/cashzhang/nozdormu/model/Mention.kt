package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder

class Mention {
    @SerializedName("text")
    @Expose
    var text: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("text", text).toString()
    }
}