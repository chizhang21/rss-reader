package com.cashzhang.ashley.bean;

import java.util.ArrayList;

public class Profile {
    private String id;
    private String client;
    private String email;
    private ArrayList<String> logins;
    private String picture;
    private String givenName;
    private String familyName;
    private String gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getLogins() {
        return logins;
    }

    public void setLogins(ArrayList<String> logins) {
        this.logins = logins;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Profile{id=" + id + " client=" + client + " email=" + email
                + " pictureAddr=" + picture + " givenName=" + givenName
                + " familyName=" + familyName + " gender=" + gender;
    }
}
