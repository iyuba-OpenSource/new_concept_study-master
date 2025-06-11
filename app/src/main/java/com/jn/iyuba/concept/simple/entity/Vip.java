package com.jn.iyuba.concept.simple.entity;

import java.util.List;

public class Vip {


    private String title;

    private String content;

    private List<VipKind> vipKindList;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<VipKind> getVipKindList() {
        return vipKindList;
    }

    public void setVipKindList(List<VipKind> vipKindList) {
        this.vipKindList = vipKindList;
    }

    public static class VipKind {

        private String name;

        private String price;

        private int month;

        public VipKind(String name, String price) {
            this.name = name;
            this.price = price;
        }

        public VipKind(String name, String price, int month) {
            this.name = name;
            this.price = price;
            this.month = month;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

}
