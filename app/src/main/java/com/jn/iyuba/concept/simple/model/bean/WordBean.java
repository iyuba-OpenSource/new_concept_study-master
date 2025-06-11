package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.concept.simple.db.ConceptWord;

import java.util.List;

public class WordBean {

    @SerializedName("size")
    private int size;
    @SerializedName("data")
    private List<ConceptWord> data;

    private int result;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ConceptWord> getData() {
        return data;
    }

    public void setData(List<ConceptWord> data) {
        this.data = data;
    }
}
