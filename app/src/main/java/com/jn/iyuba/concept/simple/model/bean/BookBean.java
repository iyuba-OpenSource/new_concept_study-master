package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.concept.simple.db.Book;

import java.util.List;

public class BookBean {

    @SerializedName("result")
    private String result;
    @SerializedName("total")
    private int total;
    @SerializedName("data")
    private List<Book> data;

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

    public List<Book> getData() {
        return data;
    }

    public void setData(List<Book> data) {
        this.data = data;
    }

}
