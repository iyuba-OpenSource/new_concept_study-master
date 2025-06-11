package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;

public class DelEvalBean {


    @SerializedName("ResultCode")
    private String resultCode;
    @SerializedName("Message")
    private String message;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
