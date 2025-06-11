package com.jn.iyuba.concept.simple.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 单词闯关的单词
 */
public class ConceptWord extends LitePalSupport implements Serializable {


    private int id;
    @SerializedName(value = "voa_id", alternate = "voaid")
    private int voaId;
    @SerializedName("word")
    private String word;
    @SerializedName("def")
    private String def;
    @SerializedName("pron")
    private String pron;
    @SerializedName("examples")
    private String examples;
    @SerializedName("audio")
    private String audio;
    @SerializedName("position")
    private String position;
    @SerializedName(value = "sentence", alternate = "Sentence")
    private String sentence;
    @SerializedName(value = "sentence_cn", alternate = {"Sentence_cn", "sentencecn"})
    private String sentenceCn;
    @SerializedName("timing")
    private String timing;
    @SerializedName(value = "end_timing", alternate = "endtiming")
    private String endTiming;
    @SerializedName(value = "sentence_audio", alternate = {"sentenceaudio", "Sentence_audio"})
    private String sentenceAudio;


    private String sentence_single_audio;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 课本id
     */

    @SerializedName(value = "bookid", alternate = "book_id")
    private int bookid;

    /**
     * 答题状态
     * 0：未答题
     * 1：回答正确
     * 2：回答错误
     */
    @Column(defaultValue = "0")
    private int answer_status;


    /**
     * 是否显示中文
     */
    @Column(ignore = true)
    private boolean isShow = false;


    /**
     * 评测后得到的链接
     */
    @Column(ignore = true)
    private String url;
    /**
     * 得分
     */
    @Column(ignore = true)
    private String total_score;


    /**
     * 收藏的标志位
     * 0： 没有收藏
     * 1： 已收藏
     */
    private int collect = 0;

    /**
     * 相同的unit_id是同一关
     */
    private int unit_id;

    /**
     * 青少版
     * 图片
     */
    private String pic_url;
    /**
     * 青少版
     */
    @SerializedName(value = "updateTime", alternate = "update_time")
    private String update_time;
    /**
     * 青少版
     */
    private String version;
    /**
     * 青少版
     */
    private int idindex;
    /**
     * 青少版
     */
    @SerializedName(value = "videoUrl", alternate = "video_url")
    private String video_url;


    public String getSentence_single_audio() {
        return sentence_single_audio;
    }

    public void setSentence_single_audio(String sentence_single_audio) {
        this.sentence_single_audio = sentence_single_audio;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getIdindex() {
        return idindex;
    }

    public void setIdindex(int idindex) {
        this.idindex = idindex;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getVoaId() {
        return voaId;
    }

    public void setVoaId(int voaId) {
        this.voaId = voaId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceCn() {
        return sentenceCn;
    }

    public void setSentenceCn(String sentenceCn) {
        this.sentenceCn = sentenceCn;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getEndTiming() {
        return endTiming;
    }

    public void setEndTiming(String endTiming) {
        this.endTiming = endTiming;
    }

    public String getSentenceAudio() {
        return sentenceAudio;
    }

    public void setSentenceAudio(String sentenceAudio) {
        this.sentenceAudio = sentenceAudio;
    }

    public int getAnswer_status() {
        return answer_status;
    }

    public void setAnswer_status(int answer_status) {
        this.answer_status = answer_status;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }
}
