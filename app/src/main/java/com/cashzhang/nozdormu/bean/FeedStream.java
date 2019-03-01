/**
  * Copyright 2018 bejson.com 
  */
package com.cashzhang.nozdormu.bean;
import java.util.List;

/**
 * Auto-generated: 2018-08-04 21:36:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class FeedStream {

    private String id;
    private String title;
    private String direction;
    private long updated;
    private List<Alternate> alternate;
    private String continuation;
    private List<FeedStreamItems> items;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setDirection(String direction) {
         this.direction = direction;
     }
     public String getDirection() {
         return direction;
     }

    public void setUpdated(long updated) {
         this.updated = updated;
     }
     public long getUpdated() {
         return updated;
     }

    public void setAlternate(List<Alternate> alternate) {
         this.alternate = alternate;
     }
     public List<Alternate> getAlternate() {
         return alternate;
     }

    public void setContinuation(String continuation) {
         this.continuation = continuation;
     }
     public String getContinuation() {
         return continuation;
     }

    public void setItems(List<FeedStreamItems> items) {
         this.items = items;
     }
     public List<FeedStreamItems> getItems() {
         return items;
     }

}