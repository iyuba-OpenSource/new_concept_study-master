package com.jn.iyuba.novel.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.novel.R;
import com.jn.iyuba.novel.entity.BookType;

import java.util.List;


public class LevelAdapter extends BaseQuickAdapter<BookType.BookLevel, BaseViewHolder> {

    private int position = -1;

    public LevelAdapter(int layoutResId, @Nullable List<BookType.BookLevel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BookType.BookLevel item) {

        helper.setText(R.id.level_tv_content, item.getContent());

        if (helper.getAdapterPosition() == position) {

            helper.setTextColor(R.id.level_tv_content, Color.WHITE);
            helper.setBackgroundColor(R.id.level_tv_content, Color.parseColor("#92CB7E"));

        } else {

            helper.setTextColor(R.id.level_tv_content, Color.parseColor("#aa222222"));
            helper.setBackgroundColor(R.id.level_tv_content, Color.WHITE);
        }
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
