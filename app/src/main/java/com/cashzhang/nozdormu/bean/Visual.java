package com.cashzhang.nozdormu.bean;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Visual implements Serializable {

    @SerializedName("processor")
    @Expose
    private String processor;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("expirationDate")
    @Expose
    private Long expirationDate;
    @SerializedName("edgeCacheUrl")
    @Expose
    private String edgeCacheUrl;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    private final static long serialVersionUID = 4317566651350877241L;

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getEdgeCacheUrl() {
        return edgeCacheUrl;
    }

    public void setEdgeCacheUrl(String edgeCacheUrl) {
        this.edgeCacheUrl = edgeCacheUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("processor", processor).append("url", url).append("width", width).append("height", height).append("expirationDate", expirationDate).append("edgeCacheUrl", edgeCacheUrl).append("contentType", contentType).toString();
    }

}