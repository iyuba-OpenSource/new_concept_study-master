package com.jn.iyuba.novel.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"types", "voaid", "chapter_order", "order_number", "level", "paraid", "index"})
public class NovelSentence {


    /**
     * bookworm，newCamstoryColor，newCamstory
     */
    @NonNull
    @ColumnInfo(name = "types")
    private String types;

    @SerializedName("BeginTiming")
    @ColumnInfo(name = "begin_timing")
    private String beginTiming;
    @SerializedName("voaid")
    @NonNull
    private String voaid;
    @SerializedName("chapter_order")
    @ColumnInfo(name = "chapter_order")
    @NonNull
    private String chapterOrder;
    @SerializedName("paraid")
    @NonNull
    private String paraid;
    @SerializedName("EndTiming")
    @ColumnInfo(name = "end_timing")
    private String endTiming;
    @SerializedName("image")
    private String image;
    @SerializedName("orderNumber")
    @ColumnInfo(name = "order_number")
    @NonNull
    private String orderNumber;
    @SerializedName("sentence_audio")
    @ColumnInfo(name = "sentence_audio")
    private String sentenceAudio;
    @SerializedName("level")
    @NonNull
    private String level;
    @SerializedName("index")
    @NonNull
    private String index;
    @SerializedName("textCH")
    @ColumnInfo(name = "text_ch")
    private String textCH;
    @SerializedName("textEN")
    @ColumnInfo(name = "text_en")
    private String textEN;


    /**
     * 得分
     */
    private String score;

    /**
     * 录音的地址
     */
    private String recordSoundUrl;

    /**
     * 发布后的shuoshuoId,用来分享
     */
    private String shuoshuoId;


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRecordSoundUrl() {
        return recordSoundUrl;
    }

    public void setRecordSoundUrl(String recordSoundUrl) {
        this.recordSoundUrl = recordSoundUrl;
    }

    public String getShuoshuoId() {
        return shuoshuoId;
    }

    public void setShuoshuoId(String shuoshuoId) {
        this.shuoshuoId = shuoshuoId;
    }

    @Nullable
    public String getTypes() {
        return types;
    }

    public void setTypes(@Nullable String types) {
        this.types = types;
    }

    public String getBeginTiming() {
        return beginTiming;
    }

    public void setBeginTiming(String beginTiming) {
        this.beginTiming = beginTiming;
    }

    public String getVoaid() {
        return voaid;
    }

    public void setVoaid(String voaid) {
        this.voaid = voaid;
    }

    public String getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(String chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public String getParaid() {
        return paraid;
    }

    public void setParaid(String paraid) {
        this.paraid = paraid;
    }

    public String getEndTiming() {
        return endTiming;
    }

    public void setEndTiming(String endTiming) {
        this.endTiming = endTiming;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSentenceAudio() {
        return sentenceAudio;
    }

    public void setSentenceAudio(String sentenceAudio) {
        this.sentenceAudio = sentenceAudio;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTextCH() {
        return textCH;
    }

    public void setTextCH(String textCH) {
        this.textCH = textCH;
    }

    public String getTextEN() {
        return textEN;
    }

    public void setTextEN(String textEN) {
        this.textEN = textEN;
    }
}
