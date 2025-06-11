package com.jn.iyuba.novel.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.novel.db.NovelBook;

import java.util.List;

public class NovelBookBean {


    @SerializedName("result")
    private int result;
    @SerializedName("data")
    private List<NovelBook> data;
    @SerializedName("message")
    private String message;


    public List<NovelBook> getData() {
        return data;
    }

    public void setData(List<NovelBook> data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
