package com.jn.iyuba.concept.simple.model.bean.me;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WordCollectListBean {


    @SerializedName("pageNumber")
    private int pageNumber;
    @SerializedName("firstPage")
    private int firstPage;
    @SerializedName("lastPage")
    private int lastPage;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("counts")
    private int counts;
    @SerializedName("totalPage")
    private int totalPage;
    @SerializedName("nextPage")
    private int nextPage;
    @SerializedName("prevPage")
    private int prevPage;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public static class DataDTO {
        @SerializedName("Word")
        private String word;
        @SerializedName("def")
        private String def;
        @SerializedName("pron")
        private String pron;
        @SerializedName("audio")
        private String audio;
        @SerializedName("createDate")
        private String createDate;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getDef() {
            return def;
        }

        public void setDef(String def) {
            this.def = def;
        }

        public String getPron() {
            return pron;
        }

        public void setPron(String pron) {
            this.pron = pron;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
