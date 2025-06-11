package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;

public class ProcessBean {


    @SerializedName("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
