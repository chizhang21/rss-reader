package com.cashzhang.nozdormu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Summary {

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("direction")
    @Expose
    private String direction;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("content", content).append("direction", direction).toString();
    }

}