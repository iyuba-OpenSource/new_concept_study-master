package com.jn.iyuba.concept.simple.db;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class Sentence extends LitePalSupport implements Serializable {
    @SerializedName("voaid")
    private String voaid;
    @SerializedName(value = "EndTiming", alternate = "endtiming")
    private String endTiming;
    @SerializedName(value = "Paraid", alternate = {"paraid", "ParaId"})
    private String paraid;
    @SerializedName(value = "IdIndex", alternate = "idindex")
    private String idIndex;
    @SerializedName(value = "Timing", alternate = "timing")
    private String timing;
    @SerializedName(value = "Sentence_cn", alternate = {"sentence_cn", "sentencecn"})
    private String sentenceCn;
    @SerializedName(value = "Sentence", alternate = "sentence")
    private String sentence;

    /**
     * 评测后的单词数据
     */
    @Column(ignore = true)
    private List<EvalWord> wordsDTOS;

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

    //青少版
    private String ImgPath;
    private String ImgWords;
    private String Start_x;
    private String End_y;
    private String End_x;
    private String Start_y;


    /**
     * 从novelSentence 转 Sentence
     */
    @Column(ignore = true)
    private String types;

    /**
     * 剑桥书虫使用
     * 获取剑桥类的句子音频
     */
    @Column(ignore = true)
    private String sentence_audio;
    /**
     * 剑桥书虫使用
     * 书籍的等级
     */
    @Column(ignore = true)
    private String level;

    /**
     * 剑桥书虫使用
     * 课本的编号
     */
    @Column(ignore = true)
    private String orderNumber;

    /**
     * 剑桥书虫使用
     * 章节的编号
     */
    @Column(ignore = true)
    private String chapter_order;


    //配音使用的变量

    /**
     * 录制的开始时间
     */
    @Column(ignore = true)
    private long startRecordTime;

    /**
     * 录制的结束时间
     *
     * @return
     */
    @Column(ignore = true)
    private long endRecordTime;

    /**
     * 是否正在录音
     */
    @Column(ignore = true)
    private boolean isRecord = false;

    /**
     * 是否正在播放
     */
    @Column(ignore = true)
    private boolean isPlaying = false;

    /**
     * 录制的位置
     */
    @Column(ignore = true)
    private long recordPosition = 0;


    public long getStartRecordTime() {
        return startRecordTime;
    }

    public void setStartRecordTime(long startRecordTime) {
        this.startRecordTime = startRecordTime;
    }

    public long getEndRecordTime() {
        return endRecordTime;
    }

    public void setEndRecordTime(long endRecordTime) {
        this.endRecordTime = endRecordTime;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public long getRecordPosition() {
        return recordPosition;
    }

    public void setRecordPosition(long recordPosition) {
        this.recordPosition = recordPosition;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getChapter_order() {
        return chapter_order;
    }

    public void setChapter_order(String chapter_order) {
        this.chapter_order = chapter_order;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSentence_audio() {
        return sentence_audio;
    }

    public void setSentence_audio(String sentence_audio) {
        this.sentence_audio = sentence_audio;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }

    public String getImgWords() {
        return ImgWords;
    }

    public void setImgWords(String imgWords) {
        ImgWords = imgWords;
    }

    public String getStart_x() {
        return Start_x;
    }

    public void setStart_x(String start_x) {
        Start_x = start_x;
    }

    public String getEnd_y() {
        return End_y;
    }

    public void setEnd_y(String end_y) {
        End_y = end_y;
    }

    public String getEnd_x() {
        return End_x;
    }

    public void setEnd_x(String end_x) {
        End_x = end_x;
    }

    public String getStart_y() {
        return Start_y;
    }

    public void setStart_y(String start_y) {
        Start_y = start_y;
    }

    public String getShuoshuoId() {
        return shuoshuoId;
    }

    public void setShuoshuoId(String shuoshuoId) {
        this.shuoshuoId = shuoshuoId;
    }

    public List<EvalWord> getWordsDTOS() {
        return wordsDTOS;
    }

    public void setWordsDTOS(List<EvalWord> wordsDTOS) {
        this.wordsDTOS = wordsDTOS;
    }

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

    public String getVoaid() {
        return voaid;
    }

    public void setVoaid(String voaid) {
        this.voaid = voaid;
    }

    public String getEndTiming() {
        return endTiming;
    }

    public void setEndTiming(String endTiming) {
        this.endTiming = endTiming;
    }

    public String getParaid() {
        return paraid;
    }

    public void setParaid(String paraid) {
        this.paraid = paraid;
    }

    public String getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(String idIndex) {
        this.idIndex = idIndex;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getSentenceCn() {
        return sentenceCn;
    }

    public void setSentenceCn(String sentenceCn) {
        this.sentenceCn = sentenceCn;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}