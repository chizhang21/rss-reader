package com.cashzhang.nozdormu.model

class Profile {
    var id: String? = null
    var client: String? = null
    var email: String? = null
    var picture: String? = null
    var givenName: String? = null
    var familyName: String? = null
    var gender: String? = null

    override fun toString(): String {
        return ("Profile{id=" + id + " client=" + client + " email=" + email
                + " pictureAddr=" + picture + " givenName=" + givenName
                + " familyName=" + familyName + " gender=" + gender)
    }
}