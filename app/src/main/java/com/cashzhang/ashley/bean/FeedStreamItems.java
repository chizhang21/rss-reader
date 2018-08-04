/**
  * Copyright 2018 bejson.com 
  */
package com.cashzhang.ashley.bean;
import java.util.List;

/**
 * Auto-generated: 2018-08-04 21:36:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class FeedStreamItems {

    private String id;
    private List<String> keywords;
    private String originId;
    private String fingerprint;
    private Content content;
    private String title;
    private long published;
    private long crawled;
    private Origin origin;
    private List<Alternate> alternate;
    private String author;
    private Summary summary;
    private List<Enclosure> enclosure;
    private Visual visual;
    private boolean unread;
    private List<Categ> categories;
    private int engagement;
    private int engagementRate;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setKeywords(List<String> keywords) {
         this.keywords = keywords;
     }
     public List<String> getKeywords() {
         return keywords;
     }

    public void setOriginId(String originId) {
         this.originId = originId;
     }
     public String getOriginId() {
         return originId;
     }

    public void setFingerprint(String fingerprint) {
         this.fingerprint = fingerprint;
     }
     public String getFingerprint() {
         return fingerprint;
     }

    public void setContent(Content content) {
         this.content = content;
     }
     public Content getContent() {
         return content;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setPublished(long published) {
         this.published = published;
     }
     public long getPublished() {
         return published;
     }

    public void setCrawled(long crawled) {
         this.crawled = crawled;
     }
     public long getCrawled() {
         return crawled;
     }

    public void setOrigin(Origin origin) {
         this.origin = origin;
     }
     public Origin getOrigin() {
         return origin;
     }

    public void setAlternate(List<Alternate> alternate) {
         this.alternate = alternate;
     }
     public List<Alternate> getAlternate() {
         return alternate;
     }

    public void setAuthor(String author) {
         this.author = author;
     }
     public String getAuthor() {
         return author;
     }

    public void setSummary(Summary summary) {
         this.summary = summary;
     }
     public Summary getSummary() {
         return summary;
     }

    public void setEnclosure(List<Enclosure> enclosure) {
         this.enclosure = enclosure;
     }
     public List<Enclosure> getEnclosure() {
         return enclosure;
     }

    public void setVisual(Visual visual) {
         this.visual = visual;
     }
     public Visual getVisual() {
         return visual;
     }

    public void setUnread(boolean unread) {
         this.unread = unread;
     }
     public boolean getUnread() {
         return unread;
     }

    public void setCategories(List<Categ> categories) {
         this.categories = categories;
     }
     public List<Categ> getCategories() {
         return categories;
     }

    public void setEngagement(int engagement) {
         this.engagement = engagement;
     }
     public int getEngagement() {
         return engagement;
     }

    public void setEngagementRate(int engagementRate) {
         this.engagementRate = engagementRate;
     }
     public int getEngagementRate() {
         return engagementRate;
     }

}