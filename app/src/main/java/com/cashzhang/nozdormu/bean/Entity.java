package com.cashzhang.nozdormu.bean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Entity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("salienceLevel")
    @Expose
    private String salienceLevel;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("mentions")
    @Expose
    private List<Mention> mentions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalienceLevel() {
        return salienceLevel;
    }

    public void setSalienceLevel(String salienceLevel) {
        this.salienceLevel = salienceLevel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Mention> getMentions() {
        return mentions;
    }

    public void setMentions(List<Mention> mentions) {
        this.mentions = mentions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("salienceLevel", salienceLevel).append("label", label).append("mentions", mentions).toString();
    }

}