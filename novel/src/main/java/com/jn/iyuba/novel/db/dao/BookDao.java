package com.jn.iyuba.novel.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jn.iyuba.novel.db.NovelBook;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM novelbook")
    List<NovelBook> getAll();


    @Insert
    void saveAll(List<NovelBook> novelBookList);


    @Insert
    void save(NovelBook novelBook);

    /**
     * 获取个数
     *
     * @param types
     * @param order_number
     * @param level
     * @return
     */
    @Query("select count(*) from novelbook where types = :types and order_number = :order_number and level = :level")
    int getCountById(String types, String order_number, String level);

    /**
     * 获取
     * @param types
     * @param order_number
     * @param level
     * @return
     */
    @Query("select * from novelbook where types = :types and order_number = :order_number and level = :level")
    NovelBook getBookForPrimaryKeys(String types, String order_number, String level);


}
