package com.cashzhang.nozdormu.bean;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Origin implements Serializable {

    @SerializedName("streamId")
    @Expose
    private String streamId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("htmlUrl")
    @Expose
    private String htmlUrl;
    private final static long serialVersionUID = -174037031250207312L;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("streamId", streamId).append("title", title).append("htmlUrl", htmlUrl).toString();
    }

}