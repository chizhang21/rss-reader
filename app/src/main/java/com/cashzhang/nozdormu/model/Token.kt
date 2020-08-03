package com.cashzhang.nozdormu.model

import java.io.Serializable

class Token : Serializable {
    var access_token: String? = null
    var refresh_token: String? = null
    var plan: String? = null
    var expires_in: String? = null
    var id: String? = null

    override fun toString(): String {
        return "access_token: $access_token refresh_token: $refresh_token id: $id"
    }
}