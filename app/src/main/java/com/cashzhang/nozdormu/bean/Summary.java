package com.cashzhang.nozdormu.bean;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Summary implements Serializable {

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("direction")
    @Expose
    private String direction;
    private final static long serialVersionUID = 8509519511516056509L;

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