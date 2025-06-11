package com.jn.iyuba.concept.simple.model.bean.sync;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncExerciseBean {


    @SerializedName("result")
    private String result;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("message")
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataDTO {
        @SerializedName("TestTime")
        private String testTime;
        @SerializedName("testindex")
        private String testindex;
        @SerializedName("Score")
        private int score;
        @SerializedName("AppId")
        private String appId;
        @SerializedName("UserAnswer")
        private String userAnswer;
        @SerializedName("LessonId")
        private String lessonId;
        @SerializedName("UpdateTime")
        private String updateTime;
        @SerializedName("BeginTime")
        private String beginTime;
        @SerializedName("TestNumber")
        private String testNumber;
        @SerializedName("TestWords")
        private String testWords;
        @SerializedName("RightAnswer")
        private String rightAnswer;
        @SerializedName("AppName")
        private String appName;

        public String getTestTime() {
            return testTime;
        }

        public void setTestTime(String testTime) {
            this.testTime = testTime;
        }

        public String getTestindex() {
            return testindex;
        }

        public void setTestindex(String testindex) {
            this.testindex = testindex;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public String getLessonId() {
            return lessonId;
        }

        public void setLessonId(String lessonId) {
            this.lessonId = lessonId;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getTestNumber() {
            return testNumber;
        }

        public void setTestNumber(String testNumber) {
            this.testNumber = testNumber;
        }

        public String getTestWords() {
            return testWords;
        }

        public void setTestWords(String testWords) {
            this.testWords = testWords;
        }

        public String getRightAnswer() {
            return rightAnswer;
        }

        public void setRightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
}
