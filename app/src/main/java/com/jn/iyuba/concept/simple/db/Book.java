package com.jn.iyuba.concept.simple.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class Book extends LitePalSupport {
    @SerializedName("Category")
    private String category;
    @SerializedName("CreateTime")
    private String createTime;
    @SerializedName("isVideo")
    private String isVideo;
    @SerializedName("pic")
    private String pic;
    @SerializedName("KeyWords")
    private String keyWords;
    @SerializedName("version")
    private String version;
    @SerializedName("DescCn")
    private String descCn;
    @SerializedName("SeriesCount")
    private String seriesCount;
    @SerializedName("SeriesName")
    private String seriesName;
    @SerializedName("UpdateTime")
    private String updateTime;
    @SerializedName("HotFlg")
    private String hotFlg;
    @SerializedName("haveMicro")
    private String haveMicro;
    @SerializedName("Id")
    private String id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(String isVideo) {
        this.isVideo = isVideo;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescCn() {
        return descCn;
    }

    public void setDescCn(String descCn) {
        this.descCn = descCn;
    }

    public String getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(String seriesCount) {
        this.seriesCount = seriesCount;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getHotFlg() {
        return hotFlg;
    }

    public void setHotFlg(String hotFlg) {
        this.hotFlg = hotFlg;
    }

    public String getHaveMicro() {
        return haveMicro;
    }

    public void setHaveMicro(String haveMicro) {
        this.haveMicro = haveMicro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}