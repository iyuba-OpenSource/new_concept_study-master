package com.jn.iyuba.concept.simple.entity;

import com.jn.iyuba.novel.db.Chapter;

public class ChapterEventbus {

    private Chapter chapter;

    /**
     * 0，更新
     * 1，跳转
     */
    private int flag;


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public ChapterEventbus(Chapter chapter) {
        this.chapter = chapter;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}
