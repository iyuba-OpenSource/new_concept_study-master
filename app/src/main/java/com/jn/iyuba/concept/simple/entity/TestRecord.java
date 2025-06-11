package com.jn.iyuba.concept.simple.entity;

/**
 */

public class TestRecord {
    public String uid;
    public String BeginTime;//测试的开始时间
    public int TitleNum;     //题号，序号
    public String UserAnswer;    //用户答案
    public String RightAnswer;    //正确答案
    public int AnswerResut;    //正确与否：0错误；1：正确
    public String TestTime;    //测试时间
    public int IsUpload;
    public int LessonId; // 本地单词库中保存的 单词的id
    public String Category; //类型
    public int TestId;
    public String TestMode; //W: 词汇 G：语法 L：听力 S：口语 R：阅读 X：写作
    public String AppName;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public int getTitleNum() {
        return TitleNum;
    }

    public void setTitleNum(int titleNum) {
        TitleNum = titleNum;
    }

    public String getUserAnswer() {
        return UserAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        UserAnswer = userAnswer;
    }

    public String getRightAnswer() {
        return RightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        RightAnswer = rightAnswer;
    }

    public int getAnswerResut() {
        return AnswerResut;
    }

    public void setAnswerResut(int answerResut) {
        AnswerResut = answerResut;
    }

    public String getTestTime() {
        return TestTime;
    }

    public void setTestTime(String testTime) {
        TestTime = testTime;
    }

    public int getIsUpload() {
        return IsUpload;
    }

    public void setIsUpload(int isUpload) {
        IsUpload = isUpload;
    }

    public int getLessonId() {
        return LessonId;
    }

    public void setLessonId(int lessonId) {
        LessonId = lessonId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getTestId() {
        return TestId;
    }

    public void setTestId(int testId) {
        TestId = testId;
    }

    public String getTestMode() {
        return TestMode;
    }

    public void setTestMode(String testMode) {
        TestMode = testMode;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }
}
