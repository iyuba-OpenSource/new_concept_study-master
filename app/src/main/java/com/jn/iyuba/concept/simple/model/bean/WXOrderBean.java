package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;

public class WXOrderBean {


    @SerializedName("appid")
    private String appid;
    @SerializedName("appsecret")
    private String appsecret;
    @SerializedName("sign")
    private String sign;
    @SerializedName("prepayid")
    private String prepayid;
    @SerializedName("mch_id")
    private String mchId;
    @SerializedName("noncestr")
    private String noncestr;
    @SerializedName("retmsg")
    private String retmsg;
    @SerializedName("mch_key")
    private String mchKey;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("retcode")
    private int retcode;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }
}
