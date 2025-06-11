package com.jn.iyuba.concept.simple.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Book;

import java.util.List;

public class ChooseBookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {

    private int position = -1;

    public ChooseBookAdapter(int layoutResId, @Nullable List<Book> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Book item) {


        helper.setText(R.id.cb_tv_desccn, item.getDescCn());
        if (position == helper.getLayoutPosition()) {

            helper.setBackgroundRes(R.id.cb_tv_desccn, R.drawable.shape_rctg_choose_book);
            helper.setTextColor(R.id.cb_tv_desccn, Color.WHITE);
        } else {

            helper.setBackgroundRes(R.id.cb_tv_desccn, R.drawable.shape_rctg_unchoose_book);
            helper.setTextColor(R.id.cb_tv_desccn, Color.BLACK);
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
