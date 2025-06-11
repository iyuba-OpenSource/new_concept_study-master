package com.jn.iyuba.concept.simple.util;

public class BookUtil {


    /**
     * 是否是青少版
     *
     * @param id
     * @return
     */
    public static boolean isYouthBook(int id) {

        if (id == 1 || id == 2 || id == 3 || id == 4) {

            return false;
        } else {

            return true;
        }
    }
}
