package com.cashzhang.nozdormu.bean;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Streams implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("updated")
    @Expose
    private Integer updated;
    @SerializedName("alternate")
    @Expose
    private List<Alternate> alternate = null;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("continuation")
    @Expose
    private String continuation;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    private final static long serialVersionUID = -381602297131992324L;

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

    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public List<Alternate> getAlternate() {
        return alternate;
    }

    public void setAlternate(List<Alternate> alternate) {
        this.alternate = alternate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getContinuation() {
        return continuation;
    }

    public void setContinuation(String continuation) {
        this.continuation = continuation;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("title", title).append("updated", updated).append("alternate", alternate).append("direction", direction).append("continuation", continuation).append("items", items).toString();
    }

}