package com.jn.iyuba.concept.simple.entity;

/**
 * Created by iyuba on 2018/11/9.
 */

public class Score {


    public String Score;
    public String category;
    public String lessontype ;
    public String testCnt ;


    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLessontype() {
        return lessontype;
    }

    public void setLessontype(String lessontype) {
        this.lessontype = lessontype;
    }

    public String getTestCnt() {
        return testCnt;
    }

    public void setTestCnt(String testCnt) {
        this.testCnt = testCnt;
    }
}
