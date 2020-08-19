package com.cashzhang.nozdormu.model

data class LoginBody(private val code: String) {

    private val client_id = "feedly"
    private val client_secret = "0XP4XQ07VVMDWBKUHTJM4WUQ"
    private val grant_type = "authorization_code"
    private val redirect_uri = "https://cloud.feedly.com/feedly.html"

}