package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.concept.simple.db.Title;

import java.util.List;

public class TitleBean {


    @SerializedName("data")
    private List<Title> data;
    @SerializedName("size")
    private int size;
    @SerializedName("result")
    private String result;
    @SerializedName("total")
    private int total;


    public List<Title> getData() {
        return data;
    }

    public void setData(List<Title> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
