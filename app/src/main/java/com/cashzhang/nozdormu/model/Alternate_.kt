package com.cashzhang.nozdormu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.builder.ToStringBuilder
import java.io.Serializable

class Alternate_ : Serializable {
    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    override fun toString(): String {
        return ToStringBuilder(this).append("href", href).append("type", type).toString()
    }

    companion object {
        private const val serialVersionUID = 6295796526951090176L
    }
}