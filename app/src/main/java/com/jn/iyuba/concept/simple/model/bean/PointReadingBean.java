package com.jn.iyuba.concept.simple.model.bean;

import android.graphics.Rect;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointReadingBean {


    @SerializedName("total")
    private String total;
    @SerializedName("Images")
    private String images;
    @SerializedName("voatext")
    private List<VoatextDTO> voatext;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<VoatextDTO> getVoatext() {
        return voatext;
    }

    public void setVoatext(List<VoatextDTO> voatext) {
        this.voatext = voatext;
    }

    public static class VoatextDTO {
        @SerializedName("ImgPath")
        private String imgPath;
        @SerializedName("EndTiming")
        private double endTiming;
        @SerializedName("ParaId")
        private String paraId;
        @SerializedName("IdIndex")
        private String idIndex;
        @SerializedName("sentence_cn")
        private String sentenceCn;
        @SerializedName("ImgWords")
        private String imgWords;
        @SerializedName("Start_x")
        private String startX;
        @SerializedName("End_y")
        private String endY;
        @SerializedName("Timing")
        private double timing;
        @SerializedName("End_x")
        private String endX;
        @SerializedName("Sentence")
        private String sentence;
        @SerializedName("Start_y")
        private String startY;

        /**
         * 对应rect
         */
        private Rect rect;


        public Rect getRect() {
            return rect;
        }

        public void setRect(Rect rect) {
            this.rect = rect;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public double getEndTiming() {
            return endTiming;
        }

        public void setEndTiming(int endTiming) {
            this.endTiming = endTiming;
        }

        public String getParaId() {
            return paraId;
        }

        public void setParaId(String paraId) {
            this.paraId = paraId;
        }

        public String getIdIndex() {
            return idIndex;
        }

        public void setIdIndex(String idIndex) {
            this.idIndex = idIndex;
        }

        public String getSentenceCn() {
            return sentenceCn;
        }

        public void setSentenceCn(String sentenceCn) {
            this.sentenceCn = sentenceCn;
        }

        public String getImgWords() {
            return imgWords;
        }

        public void setImgWords(String imgWords) {
            this.imgWords = imgWords;
        }

        public String getStartX() {
            return startX;
        }

        public void setStartX(String startX) {
            this.startX = startX;
        }

        public String getEndY() {
            return endY;
        }

        public void setEndY(String endY) {
            this.endY = endY;
        }

        public double getTiming() {
            return timing;
        }

        public void setTiming(int timing) {
            this.timing = timing;
        }

        public String getEndX() {
            return endX;
        }

        public void setEndX(String endX) {
            this.endX = endX;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getStartY() {
            return startY;
        }

        public void setStartY(String startY) {
            this.startY = startY;
        }
    }
}
