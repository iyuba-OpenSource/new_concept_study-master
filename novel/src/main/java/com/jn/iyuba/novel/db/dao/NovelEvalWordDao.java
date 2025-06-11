package com.jn.iyuba.novel.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jn.iyuba.novel.db.NovelEvalWord;

import java.util.List;

@Dao
public interface NovelEvalWordDao {


    /**
     * 删除某一句的评测单词
     *
     * @param types
     * @param voaid
     * @param paraid
     * @param senIndex
     * @param chapter_order
     * @param order_number
     * @param level
     */
    @Query("delete from novelevalword where types = :types and voaid = :voaid and paraid = :paraid " +
            "and senIndex = :senIndex and chapter_order = :chapter_order and order_number = :order_number and level = :level")
    void deleteSentenceWord(String types, String voaid, String paraid, String senIndex, String chapter_order, String order_number, String level);


    /**
     * 插入单调数据
     *
     * @param novelEvalWord
     */
    @Insert
    void insertSingle(NovelEvalWord novelEvalWord);


    /**
     * 获取某句评测的数据
     *
     * @param types
     * @param voaid
     * @param paraid
     * @param senIndex
     * @param chapter_order
     * @param order_number
     * @param level
     * @return
     */
    @Query("select * from NovelEvalWord where types = :types and voaid = :voaid and paraid = :paraid and senIndex = :senIndex and chapter_order = :chapter_order " +
            "and order_number = :order_number and level = :level")
    List<NovelEvalWord> getEvalWord(String types, String voaid, String paraid, String senIndex, String chapter_order, String order_number, String level);
}
