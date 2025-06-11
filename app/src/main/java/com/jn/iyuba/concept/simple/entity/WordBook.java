package com.jn.iyuba.concept.simple.entity;

public class WordBook {

    private String name;

    private int bookid;

    private int num;


    public WordBook(String name, int bookid, int num) {
        this.name = name;
        this.bookid = bookid;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
