package com.jn.iyuba.novel.model;

import com.jn.iyuba.novel.model.bean.NovelBookBean;
import com.jn.iyuba.novel.model.bean.ChapterBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServer {


    /**
     * 获取章节
     *
     * @param types       book
     * @param level
     * @param orderNumber
     * @param from        bookworm，newCamstoryColor，newCamstory
     * @return
     */
    @GET("/book/getStroryInfo.jsp")
    Observable<ChapterBean> getStroryInfo(@Query("types") String types, @Query("level") int level,
                                          @Query("orderNumber") int orderNumber, @Query("from") String from);


    /**
     * 获取课本
     *
     * @param types
     * @param level
     * @param from
     * @return
     */
    @GET("/book/getStroryInfo.jsp")
    Observable<NovelBookBean> getStroryInfo(@Query("types") String types, @Query("level") int level, @Query("from") String from);
}
