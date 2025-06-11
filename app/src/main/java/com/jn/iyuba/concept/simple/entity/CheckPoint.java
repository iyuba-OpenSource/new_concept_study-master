package com.jn.iyuba.concept.simple.entity;

public class CheckPoint {

    private int bookid;

    private int unit_id;

    private int voaid;

    /**
     * 是否通关
     */
    private boolean isPass;

    /**
     * 单词正确的数量
     */
    private int tCount;

    /**
     * 单词总数
     */
    private int total;


    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public int gettCount() {
        return tCount;
    }

    public void settCount(int tCount) {
        this.tCount = tCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getVoaid() {
        return voaid;
    }

    public void setVoaid(int voaid) {
        this.voaid = voaid;
    }
}
