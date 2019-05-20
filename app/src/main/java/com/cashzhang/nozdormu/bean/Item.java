package com.cashzhang.nozdormu.bean;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Item implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("keywords")
    @Expose
    private List<String> keywords = null;
    @SerializedName("originId")
    @Expose
    private String originId;
    @SerializedName("fingerprint")
    @Expose
    private String fingerprint;
    @SerializedName("content")
    @Expose
    @Nullable
    private Content content;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("alternate")
    @Expose
    private List<Alternate_> alternate = null;
    @SerializedName("crawled")
    @Expose
    private Double crawled;
    @SerializedName("published")
    @Expose
    private Double published;
    @SerializedName("origin")
    @Expose
    private Origin origin;
    @SerializedName("visual")
    @Expose
    private Visual visual;
    @SerializedName("canonicalUrl")
    @Expose
    private String canonicalUrl;
    @SerializedName("unread")
    @Expose
    private Boolean unread;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    private final static long serialVersionUID = -5040485178574463095L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Alternate_> getAlternate() {
        return alternate;
    }

    public void setAlternate(List<Alternate_> alternate) {
        this.alternate = alternate;
    }

    public Double getCrawled() {
        return crawled;
    }

    public void setCrawled(Double crawled) {
        this.crawled = crawled;
    }

    public Double getPublished() {
        return published;
    }

    public void setPublished(Double published) {
        this.published = published;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Visual getVisual() {
        return visual;
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("keywords", keywords).append("originId", originId).append("fingerprint", fingerprint).append("content", content).append("title", title).append("author", author).append("summary", summary).append("alternate", alternate).append("crawled", crawled).append("published", published).append("origin", origin).append("visual", visual).append("canonicalUrl", canonicalUrl).append("unread", unread).append("categories", categories).toString();
    }

}