package com.cashzhang.ashley.bean;

import java.util.ArrayList;
import java.util.List;

public class CategItem {
    private String id;
    private String title;
    private String websie;
    private List<Categ> categories = new ArrayList<Categ>();
    private String uodated;
    private String subscribers;
    private String velocity;
    private String contentType;
    private String iconUrl;
    private String partial;
    private String visualUrl;

    @Override
    public String toString() {
        return "FeedItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", websie='" + websie + '\'' +
                ", categories=" + categories +
                ", uodated='" + uodated + '\'' +
                ", subscribers='" + subscribers + '\'' +
                ", velocity='" + velocity + '\'' +
                ", contentType='" + contentType + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", partial='" + partial + '\'' +
                ", visualUrl='" + visualUrl + '\'' +
                '}';
    }

    public CategItem() {
    }

    public CategItem(String id, String title, String websie, List<Categ> categories, String uodated, String subscribers, String velocity, String contentType, String iconUrl, String partial, String visualUrl) {
        this.id = id;
        this.title = title;
        this.websie = websie;
        this.categories = categories;
        this.uodated = uodated;
        this.subscribers = subscribers;
        this.velocity = velocity;
        this.contentType = contentType;
        this.iconUrl = iconUrl;
        this.partial = partial;
        this.visualUrl = visualUrl;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsie() {
        return websie;
    }

    public void setWebsie(String websie) {
        this.websie = websie;
    }

    public List<Categ> getCategories() {
        return categories;
    }

    public void setCategories(List<Categ> categories) {
        this.categories = categories;
    }

    public String getUodated() {
        return uodated;
    }

    public void setUodated(String uodated) {
        this.uodated = uodated;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public String getVelocity() {
        return velocity;
    }

    public void setVelocity(String velocity) {
        this.velocity = velocity;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPartial() {
        return partial;
    }

    public void setPartial(String partial) {
        this.partial = partial;
    }

    public String getVisualUrl() {
        return visualUrl;
    }

    public void setVisualUrl(String visualUrl) {
        this.visualUrl = visualUrl;
    }
}
