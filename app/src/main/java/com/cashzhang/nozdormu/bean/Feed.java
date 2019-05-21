package com.cashzhang.nozdormu.bean;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Feed implements Serializable {

    private static final long serialVersionUID = 6554L;

    @SerializedName("feedId")
    @Expose
    private String feedId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("updated")
    @Expose
    private Long updated;
    @SerializedName("velocity")
    @Expose
    private Double velocity;
    @SerializedName("topics")
    @Expose
    private List<String> topics = null;
    @SerializedName("subscribers")
    @Expose
    private Integer subscribers;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("partial")
    @Expose
    private Boolean partial;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("estimatedEngagement")
    @Expose
    private Integer estimatedEngagement;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("visualUrl")
    @Expose
    private String visualUrl;
    @SerializedName("coverUrl")
    @Expose
    private String coverUrl;
    @SerializedName("coverColor")
    @Expose
    private String coverColor;

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
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

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getPartial() {
        return partial;
    }

    public void setPartial(Boolean partial) {
        this.partial = partial;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEstimatedEngagement() {
        return estimatedEngagement;
    }

    public void setEstimatedEngagement(Integer estimatedEngagement) {
        this.estimatedEngagement = estimatedEngagement;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getVisualUrl() {
        return visualUrl;
    }

    public void setVisualUrl(String visualUrl) {
        this.visualUrl = visualUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverColor() {
        return coverColor;
    }

    public void setCoverColor(String coverColor) {
        this.coverColor = coverColor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("feedId", feedId).append("id", id).append("title", title).append("updated", updated).append("velocity", velocity).append("topics", topics).append("subscribers", subscribers).append("website", website).append("partial", partial).append("language", language).append("state", state).append("contentType", contentType).append("description", description).append("estimatedEngagement", estimatedEngagement).append("iconUrl", iconUrl).append("visualUrl", visualUrl).append("coverUrl", coverUrl).append("coverColor", coverColor).toString();
    }

}