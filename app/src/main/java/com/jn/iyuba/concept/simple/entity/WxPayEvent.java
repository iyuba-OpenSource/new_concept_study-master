package com.jn.iyuba.concept.simple.entity;

public class WxPayEvent {

    private int errcode;


    public WxPayEvent(int errcode) {
        this.errcode = errcode;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }
}
