package com.jn.iyuba.concept.simple.entity;

/**
 * 刷新下载进度
 */
public class DownloadRefresh {


    private int bookid;

    private int voaid;


    public DownloadRefresh(int bookid, int voaid) {
        this.bookid = bookid;
        this.voaid = voaid;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public DownloadRefresh(int voaid) {
        this.voaid = voaid;
    }

    public int getVoaid() {
        return voaid;
    }

    public void setVoaid(int voaid) {
        this.voaid = voaid;
    }
}
