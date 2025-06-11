package com.jn.iyuba.concept.simple.model.bean.sync;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncWordBean {


    /**
     * {
     *     "result": 1,
     *     "mode": "2",
     *     "totalRight": 28,
     *     "msg": "Success",
     *     "uid": 13801378,
     *     "dataWrong": [
     *         {
     *             "score": 0,
     *             "userAnswer": "入ります",
     *             "testMode": "W",
     *             "testTime": "2023-02-14 11:23:12.0",
     *             "id": 9213,
     *             "lessonName": "jp3"
     *         },
     *         {
     *             "score": 0,
     *             "userAnswer": "马上，一会儿，不久",
     *             "testMode": "W",
     *             "testTime": "2023-02-14 11:23:15.0",
     *             "id": 9214,
     *             "lessonName": "jp3"
     *         }
     *     ],
     *     "testMode": "W",
     *     "lesson": "jp3",
     *     "dataRight": [
     *         {
     *             "score": 1,
     *             "userAnswer": "中国人",
     *             "testMode": "W",
     *             "testTime": "2023-02-14 11:22:16.0",
     *             "id": 9191,
     *             "lessonName": "jp3"
     *         },
     *         {
     *             "score": 1,
     *             "userAnswer": "日本人",
     *             "testMode": "W",
     *             "testTime": "2023-02-14 11:22:19.0",
     *             "id": 9192,
     *             "lessonName": "jp3"
     *         }
     *     ],
     *     "totalWrong": 2
     * }
     */
    @SerializedName("result")
    private int result;
    @SerializedName("mode")
    private String mode;
    @SerializedName("totalRight")
    private int totalRight;
    @SerializedName("msg")
    private String msg;
    @SerializedName("uid")
    private int uid;
    @SerializedName("dataWrong")
    private List<WordDataDTO> dataWrong;
    @SerializedName("testMode")
    private String testMode;
    @SerializedName("lesson")
    private String lesson;
    @SerializedName("dataRight")
    private List<WordDataDTO> dataRight;
    @SerializedName("totalWrong")
    private int totalWrong;


    public List<WordDataDTO> getDataWrong() {
        return dataWrong;
    }

    public void setDataWrong(List<WordDataDTO> dataWrong) {
        this.dataWrong = dataWrong;
    }

    public List<WordDataDTO> getDataRight() {
        return dataRight;
    }

    public void setDataRight(List<WordDataDTO> dataRight) {
        this.dataRight = dataRight;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(int totalRight) {
        this.totalRight = totalRight;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }


    public int getTotalWrong() {
        return totalWrong;
    }

    public void setTotalWrong(int totalWrong) {
        this.totalWrong = totalWrong;
    }


    public static class WordDataDTO {

        @SerializedName("TestId")
        private String testId;
        @SerializedName("score")
        private int score;
        @SerializedName("userAnswer")
        private String userAnswer;
        @SerializedName("testMode")
        private String testMode;
        @SerializedName("LessonId")
        private String lessonId;
        @SerializedName("testTime")
        private String testTime;
        @SerializedName("Lesson")
        private String lesson;

        public String getTestId() {
            return testId;
        }

        public void setTestId(String testId) {
            this.testId = testId;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public String getTestMode() {
            return testMode;
        }

        public void setTestMode(String testMode) {
            this.testMode = testMode;
        }

        public String getLessonId() {
            return lessonId;
        }

        public void setLessonId(String lessonId) {
            this.lessonId = lessonId;
        }

        public String getTestTime() {
            return testTime;
        }

        public void setTestTime(String testTime) {
            this.testTime = testTime;
        }

        public String getLesson() {
            return lesson;
        }

        public void setLesson(String lesson) {
            this.lesson = lesson;
        }
    }
}
