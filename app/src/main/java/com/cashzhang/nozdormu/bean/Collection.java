package com.cashzhang.nozdormu.bean;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Collection implements Serializable {

    private static final long serialVersionUID = 3151212532091514L;

    @SerializedName("customizable")
    @Expose
    private Boolean customizable;
    @SerializedName("enterprise")
    @Expose
    private Boolean enterprise;
    @SerializedName("feeds")
    @Expose
    private List<Feed> feeds = null;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("numFeeds")
    @Expose
    private Integer numFeeds;
    @SerializedName("id")
    @Expose
    private String id;

    public Boolean getCustomizable() {
        return customizable;
    }

    public void setCustomizable(Boolean customizable) {
        this.customizable = customizable;
    }

    public Boolean getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Boolean enterprise) {
        this.enterprise = enterprise;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getNumFeeds() {
        return numFeeds;
    }

    public void setNumFeeds(Integer numFeeds) {
        this.numFeeds = numFeeds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("customizable", customizable).append("enterprise", enterprise).append("feeds", feeds).append("label", label).append("numFeeds", numFeeds).append("id", id).toString();
    }

}