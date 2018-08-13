/**
 * Copyright 2018 bejson.com
 */
package com.cashzhang.ashley.bean;
import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2018-08-12 17:46:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class MarkAsRead implements Serializable {

    private String action;
    private String type;
    private List<String> entryIds;
    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return action;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setEntryIds(List<String> entryIds) {
        this.entryIds = entryIds;
    }
    public List<String> getEntryIds() {
        return entryIds;
    }

}