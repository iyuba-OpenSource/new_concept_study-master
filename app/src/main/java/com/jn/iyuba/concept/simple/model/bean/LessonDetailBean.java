package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.concept.simple.db.Sentence;

import java.util.List;

public class LessonDetailBean {


    @SerializedName("data")
    private List<Sentence> data;
    @SerializedName("size")
    private int size;
    @SerializedName("title")
    private TitleDTO title;

    //青少版
    private String total;

    private String Images;

    private List<Sentence> voatext;


    public List<Sentence> getVoatext() {
        return voatext;
    }

    public void setVoatext(List<Sentence> voatext) {
        this.voatext = voatext;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public List<Sentence> getData() {
        return data;
    }

    public void setData(List<Sentence> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public TitleDTO getTitle() {
        return title;
    }

    public void setTitle(TitleDTO title) {
        this.title = title;
    }

    public static class TitleDTO {
        @SerializedName("title_cn")
        private String titleCn;
        @SerializedName("title")
        private String title;

        public String getTitleCn() {
            return titleCn;
        }

        public void setTitleCn(String titleCn) {
            this.titleCn = titleCn;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
