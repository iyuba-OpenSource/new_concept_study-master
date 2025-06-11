package com.jn.iyuba.concept.simple.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 课文练习，现有填空题和单选题
 */
public class Exercise extends LitePalSupport {
    @SerializedName("question")
    private String question;
    @SerializedName("answer")
    private String answer;
    @SerializedName(value = "voa_id", alternate = {"id", "voaid"})
    private String voaId;
    @SerializedName(value = "choice_B", alternate = "choiceb")
    private String choiceB;
    @SerializedName(value = "choice_C", alternate = "choicec")
    private String choiceC;
    @SerializedName(value = "choice_D", alternate = "choiced")
    private String choiceD;
    @SerializedName(value = "index_id", alternate = "indexid")
    private String indexId;
    @SerializedName(value = "choice_A", alternate = "choicea")
    private String choiceA;


    @SerializedName("number")
    private String number;
    @SerializedName("note")
    private String note;
    @SerializedName(value = "ques_num", alternate = "quesnum")
    private String quesNum;
    @SerializedName(value = "desc_CH",alternate = "descch")
    private String descCh;
    @SerializedName("column")
    private String column;
    @SerializedName(value = "desc_EN",alternate = "descen")
    private String descEn;
    @SerializedName("type")
    private String type;


    private int upload = 0;
    /**
     * 选择的选项
     */
    private String checkAnswer;

    /**
     * 选择的位置
     */
    private int checkPosition = -1;

    /**
     * 开始时间
     */
    @Column(ignore = true)
    private String BeginTime;

    /**
     * 做题时间
     */
    @Column(ignore = true)
    private String TestTime;


    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public String getCheckAnswer() {
        return checkAnswer;
    }

    public void setCheckAnswer(String checkAnswer) {
        this.checkAnswer = checkAnswer;
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getTestTime() {
        return TestTime;
    }

    public void setTestTime(String testTime) {
        TestTime = testTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVoaId() {
        return voaId;
    }

    public void setVoaId(String voaId) {
        this.voaId = voaId;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getQuesNum() {
        return quesNum;
    }

    public void setQuesNum(String quesNum) {
        this.quesNum = quesNum;
    }

    public String getDescCh() {
        return descCh;
    }

    public void setDescCh(String descCh) {
        this.descCh = descCh;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }


    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}