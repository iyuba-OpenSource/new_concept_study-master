package com.jn.iyuba.novel.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jn.iyuba.novel.db.NovelSentence;

import java.util.List;

@Dao
public interface NovelSentenceDao {


    @Insert
    void addSentence(NovelSentence novelSentence);


    /**
     * 这个句子是否存在
     *
     * @param types
     * @param voaid
     * @param chapter_order
     * @param order_number
     * @param level
     * @param paraid
     * @param index
     * @return
     */
    @Query("select count(*) from  NovelSentence where types=:types and voaid =:voaid and" +
            " chapter_order =:chapter_order and order_number= :order_number and level =:level and paraid = :paraid and 'index' = :index")
    int getCountByPrimaryKeys(String types, String voaid, String chapter_order, String order_number, String level, String paraid, String index);


    /**
     * 获取这个句子的数据
     *
     * @param types
     * @param voaid
     * @param chapter_order
     * @param order_number
     * @param level
     * @param paraid
     * @param indexx        由于是关键字，所有的字段都必须添加双引号
     * @return
     */
    @Query("select * from  NovelSentence where \"types\"=:types and \"voaid\" =:voaid and" +
            " \"chapter_order\" =:chapter_order and \"order_number\"= :order_number and \"level\" =:level and \"paraid\" = :paraid and \"index\" = :indexx")
    NovelSentence getSentenceByPrimaryKeys(String types, String voaid, String chapter_order, String order_number, String level, String paraid, String indexx);


    /**
     * 更新句子
     *
     * @param novelSentence
     */
    @Update
    void updateSingle(NovelSentence novelSentence);


    /**
     * 是否有这个章节的句子
     *
     * @param types
     * @param voaid
     * @param chapter_order
     * @param order_number
     * @param level
     * @return
     */
    @Query("select count(*) from  NovelSentence where types=:types and voaid =:voaid and" +
            " chapter_order =:chapter_order and order_number= :order_number and level =:level")
    int getCountForChapter(String types, String voaid, String chapter_order, String order_number, String level);


    /**
     * 获取本章节评测的数量
     *
     * @param types
     * @param voaid
     * @param chapter_order
     * @param order_number
     * @param level
     * @return
     */
    @Query("select count(*) from  NovelSentence where types=:types and voaid =:voaid and" +
            " chapter_order =:chapter_order and order_number= :order_number and level =:level and score is not null")
    int getCountForEval(String types, String voaid, String chapter_order, String order_number, String level);

    /**
     * 获取这个章节的句子
     *
     * @param types
     * @param voaid
     * @param chapter_order
     * @param order_number
     * @param level
     * @return
     */
    @Query("select * from  NovelSentence where \"types\"=:types and \"voaid\" =:voaid and" +
            " \"chapter_order\" =:chapter_order and \"order_number\"= :order_number and \"level\" =:level ORDER BY ABS(\"paraid\"),ABS(\"index\")")
    List<NovelSentence> getDataByPrimaryKeys(String types, String voaid, String chapter_order, String order_number, String level);
}
