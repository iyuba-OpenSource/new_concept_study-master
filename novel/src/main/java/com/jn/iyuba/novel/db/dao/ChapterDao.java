package com.jn.iyuba.novel.db.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jn.iyuba.novel.db.Chapter;

import java.util.List;

@Dao
public interface ChapterDao {


    @Query("SELECT * FROM chapter")
    List<Chapter> getAll();

    @Insert
    void insert(List<Chapter> chapterList);

    /**
     * 插入单条数据
     *
     * @param chapterList
     */
    @Insert
    void insertSingle(Chapter chapterList);

    /**
     * 查询此条数据是否存在
     *
     * @param voaid
     * @param types
     */
    @Query("select count(*) from chapter where voaid = :voaid and types = :types and level =:level and" +
            " order_number =:order_number and chapter_order = :chapter_order")
    int getCountByID(String voaid, String types, String level, String order_number, String chapter_order);


    /**
     * 获取单条数据
     *
     * @param voaid
     * @param types
     * @param level
     * @param order_number
     * @param chapter_order
     * @return
     */
    @Query("select * from chapter where  voaid = :voaid and types = :types and level =:level and " +
            "order_number =:order_number and chapter_order = :chapter_order")
    Chapter getSingleByPrimaryKeys(String voaid, String types, String level, String order_number, String chapter_order);


    /**
     * 更新听力进度
     *
     * @param test_number
     * @param end_flg
     * @param voaid
     * @param types
     * @param level
     * @param order_number
     * @param chapter_order
     */
    @Query("update chapter set test_number = :test_number,end_flg = :end_flg where voaid = :voaid and types = :types and level =:level and " +
            " order_number =:order_number and chapter_order = :chapter_order")
    void updateProgress(String test_number, String end_flg, String voaid, String types, String level, String order_number, String chapter_order);

    /**
     * 从一本书中随机获取某一章节
     *
     * @param types
     * @param level
     * @param order_number
     * @return
     */
    @Query("select * from chapter where types =:types and level = :level and order_number = :order_number order by RANDOM() limit 1")
    Chapter getChapterForRandom(String types, String level, String order_number);


    /**
     * 顺序获取当前voaid的下一个数据
     *
     * @param types
     * @param level
     * @param order_number
     * @param voaid
     * @return
     */
    @Query("select * from chapter where types =:types and level = :level and order_number = :order_number and voaid >:voaid order by voaid limit 1")
    Chapter getChapterForOrder(String types, String level, String order_number, String voaid);


    /**
     * voaid排序  获取上一个数据
     *
     * @param types
     * @param level
     * @param order_number
     * @param voaid
     * @return
     */
    @Query("select * from chapter where types =:types and level =:level and order_number = :order_number and voaid <:voaid order by voaid DESC limit 1")
    Chapter getChapterPre(String types, String level, String order_number, String voaid);

    /**
     * 获取第一条数据，
     *
     * @param types
     * @param level
     * @param order_number
     * @return
     */
    @Query("select * from chapter where types =:types and level = :level and order_number = :order_number order by voaid limit 1")
    Chapter getFirstChapterForBook(String types, String level, String order_number);


    /**
     * 获取最后一条数据，
     *
     * @param types
     * @param level
     * @param order_number
     * @return
     */
    @Query("select * from chapter where types =:types and level = :level and order_number = :order_number order by voaid DESC limit 1")
    Chapter getLastChapterForBook(String types, String level, String order_number);
}
