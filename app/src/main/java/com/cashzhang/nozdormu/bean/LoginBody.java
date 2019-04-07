package com.cashzhang.nozdormu.bean;

public class LoginBody {

    private String client_id;
    private String client_secret;
    private String grant_type;
    private String redirect_uri;
    private String code;

    public LoginBody (String code) {
        this.client_id = "feedly";
        this.client_secret = "0XP4XQ07VVMDWBKUHTJM4WUQ";
        this.grant_type = "authorization_code";
        this.redirect_uri = "https://cloud.feedly.com/feedly.html";
        this.code = code;
    }
}
