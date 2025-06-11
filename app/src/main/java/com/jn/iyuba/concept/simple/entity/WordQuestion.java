package com.jn.iyuba.concept.simple.entity;

import com.jn.iyuba.concept.simple.db.ConceptWord;

import java.util.List;

public class WordQuestion {

    private ConceptWord conceptWord;

    private List<ConceptWord> answerList;

    /**
     * 设置题目的语言
     * type
     * 0：中文
     * 1：英文
     */
    private int type;

    /**
     * 选择位置
     */
    private int choosePosition = -1;

    /**
     * 正确的位置
     */
    private int tPosition;

    /**
     * 考试时间
     */
    private String beginTime;

    /**
     * 测试时间，答题时间
     */
    private String testTime;


    public int gettPosition() {
        return tPosition;
    }

    public void settPosition(int tPosition) {
        this.tPosition = tPosition;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public int getChoosePosition() {
        return choosePosition;
    }

    public void setChoosePosition(int choosePosition) {
        this.choosePosition = choosePosition;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ConceptWord getWord() {
        return conceptWord;
    }

    public void setWord(ConceptWord conceptWord) {
        this.conceptWord = conceptWord;
    }

    public List<ConceptWord> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<ConceptWord> answerList) {
        this.answerList = answerList;
    }
}
