package com.jn.iyuba.novel.entity;

import java.util.List;

public class BookType {


    private String name;
    /**
     * bookworm，newCamstoryColor，newCamstory
     */
    private String from;

    private List<BookLevel> bookLevelList;


    public BookType(String name, String from, List<BookLevel> bookLevelList) {
        this.name = name;
        this.from = from;
        this.bookLevelList = bookLevelList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<BookLevel> getBookLevelList() {
        return bookLevelList;
    }

    public void setBookLevelList(List<BookLevel> bookLevelList) {
        this.bookLevelList = bookLevelList;
    }

    public static class BookLevel {


        private String content;

        private int level;

        private String from;


        public BookLevel(String content, int level) {
            this.content = content;
            this.level = level;
        }

        public BookLevel(String content, int level, String from) {
            this.content = content;
            this.level = level;
            this.from = from;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
