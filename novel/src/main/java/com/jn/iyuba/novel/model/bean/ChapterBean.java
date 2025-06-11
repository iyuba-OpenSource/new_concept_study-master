package com.jn.iyuba.novel.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.novel.db.NovelBook;
import com.jn.iyuba.novel.db.Chapter;

import java.util.List;

public class ChapterBean {

    @SerializedName("bookInfo")
    private NovelBook novelBookInfo;
    @SerializedName("result")
    private int result;
    @SerializedName("chapterInfo")
    private List<Chapter> chapterInfo;
    @SerializedName("message")
    private String message;


    public NovelBook getBookInfo() {
        return novelBookInfo;
    }

    public void setBookInfo(NovelBook novelBookInfo) {
        this.novelBookInfo = novelBookInfo;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<Chapter> getChapterInfo() {
        return chapterInfo;
    }

    public void setChapterInfo(List<Chapter> chapterInfo) {
        this.chapterInfo = chapterInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
