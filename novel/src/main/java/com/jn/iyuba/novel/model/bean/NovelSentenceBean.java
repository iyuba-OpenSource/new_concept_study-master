package com.jn.iyuba.novel.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.novel.db.NovelSentence;

import java.util.List;

public class NovelSentenceBean {


    @SerializedName("result")
    private int result;
    @SerializedName("texts")
    private List<NovelSentence> texts;
    @SerializedName("message")
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<NovelSentence> getTexts() {
        return texts;
    }

    public void setTexts(List<NovelSentence> texts) {
        this.texts = texts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
