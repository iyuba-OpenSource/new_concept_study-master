package com.jn.iyuba.novel.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 章节
 */
@Entity(primaryKeys = {"types", "voaid", "level", "order_number", "chapter_order"})
public class Chapter implements Serializable {


    /**
     * bookworm，newCamstoryColor，newCamstory
     */
    @NonNull
    private String types;

    @SerializedName("voaid")
    @NonNull
    private String voaid;
    @SerializedName("orderNumber")
    @NonNull
    private String order_number;
    @SerializedName("level")
    @NonNull
    private String level;
    @SerializedName("chapterOrder")
    @NonNull
    private String chapter_order;
    @SerializedName("sound")
    private String sound;
    @SerializedName("show")
    private int show;
    @SerializedName("cname_cn")
    private String cname_cn;
    @SerializedName("cname_en")
    private String cname_en;


    /**
     * 首页进度
     * 读到第几句
     */
    @ColumnInfo(defaultValue = "0")
    private int test_number;

    /**
     * 是否播放完成
     */
    @ColumnInfo(defaultValue = "0")
    private int end_flg;

    /**
     * 用于adapter更新数据
     */
    @Ignore
    private boolean isUpdate = false;

    /**
     * 句子数量
     */
    @Ignore
    private int sentenceTotal = 0;

    /**
     * 评测的数量
     */
    @Ignore
    private int evalCount;


    public int getEvalCount() {
        return evalCount;
    }

    public void setEvalCount(int evalCount) {
        this.evalCount = evalCount;
    }

    public int getSentenceTotal() {
        return sentenceTotal;
    }

    public void setSentenceTotal(int sentenceTotal) {
        this.sentenceTotal = sentenceTotal;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public int getTest_number() {
        return test_number;
    }

    public void setTest_number(int test_number) {
        this.test_number = test_number;
    }

    public int getEnd_flg() {
        return end_flg;
    }

    public void setEnd_flg(int end_flg) {
        this.end_flg = end_flg;
    }

    @NonNull
    public String getTypes() {
        return types;
    }

    public void setTypes(@NonNull String types) {
        this.types = types;
    }

    public String getVoaid() {
        return voaid;
    }

    public void setVoaid(String voaid) {
        this.voaid = voaid;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getChapter_order() {
        return chapter_order;
    }

    public void setChapter_order(String chapter_order) {
        this.chapter_order = chapter_order;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getCname_cn() {
        return cname_cn;
    }

    public void setCname_cn(String cname_cn) {
        this.cname_cn = cname_cn;
    }

    public String getCname_en() {
        return cname_en;
    }

    public void setCname_en(String cname_en) {
        this.cname_en = cname_en;
    }
}
