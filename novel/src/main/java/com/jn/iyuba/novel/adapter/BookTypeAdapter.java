package com.jn.iyuba.novel.adapter;

import com.contrarywind.adapter.WheelAdapter;
import com.jn.iyuba.novel.entity.BookType;

import java.util.List;

/**
 * 书类型
 */

public class BookTypeAdapter implements WheelAdapter<String> {

    List<BookType> bookTypeList;

    public BookTypeAdapter(List<BookType> bookTypeList) {
        this.bookTypeList = bookTypeList;
    }

    @Override
    public int getItemsCount() {
        return bookTypeList.size();
    }

    @Override
    public String getItem(int index) {
        return bookTypeList.get(index).getName();
    }

    @Override
    public int indexOf(String o) {

        int index = 0;
        for (int i = 0; i < bookTypeList.size(); i++) {

            BookType type = bookTypeList.get(i);
            if (type.getName().equals(o)) {

                index = i;
                break;
            }
        }
        return index;
    }


    public List<BookType> getStringList() {
        return bookTypeList;
    }

    public void setStringList(List<BookType> bookTypeList) {
        this.bookTypeList = bookTypeList;
    }
}
