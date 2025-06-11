package com.jn.iyuba.novel.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * 课本的数据类
 */
@Entity(primaryKeys = {"types", "order_number", "level"})
public class NovelBook {

    /**
     * bookworm，newCamstoryColor，newCamstory
     */
    @NonNull
    @ColumnInfo(name = "types")
    private String types;

    @SerializedName("orderNumber")
    @ColumnInfo(name = "order_number")
    @NonNull
    private String order_number;
    @ColumnInfo(name = "level")
    @SerializedName("level")
    @NonNull
    private String level;
    @ColumnInfo(name = "book_name_en")
    @SerializedName("bookname_en")
    private String book_name_en;
    @SerializedName("author")
    @ColumnInfo(name = "author")
    private String author;
    @SerializedName("about_book")
    @ColumnInfo(name = "about_book")
    private String about_book;
    @SerializedName("bookname_cn")
    @ColumnInfo(name = "book_name_cn")
    private String book_name_cn;
    @SerializedName("about_interpreter")
    @ColumnInfo(name = "about_interpreter")
    private String about_interpreter;
    @SerializedName("wordcounts")
    @ColumnInfo(name = "word_counts")
    private String word_counts;
    @SerializedName("interpreter")
    @ColumnInfo(name = "interpreter")
    private String interpreter;
    @SerializedName("pic")
    @ColumnInfo(name = "pic")
    private String pic;
    @SerializedName("about_author")
    @ColumnInfo(name = "about_author")
    private String about_author;


    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBook_name_en() {
        return book_name_en;
    }

    public void setBook_name_en(String book_name_en) {
        this.book_name_en = book_name_en;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAbout_book() {
        return about_book;
    }

    public void setAbout_book(String about_book) {
        this.about_book = about_book;
    }

    public String getBook_name_cn() {
        return book_name_cn;
    }

    public void setBook_name_cn(String book_name_cn) {
        this.book_name_cn = book_name_cn;
    }

    public String getAbout_interpreter() {
        return about_interpreter;
    }

    public void setAbout_interpreter(String about_interpreter) {
        this.about_interpreter = about_interpreter;
    }

    public String getWord_counts() {
        return word_counts;
    }

    public void setWord_counts(String word_counts) {
        this.word_counts = word_counts;
    }

    public String getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAbout_author() {
        return about_author;
    }

    public void setAbout_author(String about_author) {
        this.about_author = about_author;
    }
}
