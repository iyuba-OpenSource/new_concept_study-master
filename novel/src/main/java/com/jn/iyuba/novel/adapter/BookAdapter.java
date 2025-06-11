package com.jn.iyuba.novel.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.novel.NovelConstant;
import com.jn.iyuba.novel.NovelApplication;
import com.jn.iyuba.novel.R;
import com.jn.iyuba.novel.db.NovelBook;

import java.util.List;


/**
 * 课本
 */
public class BookAdapter extends BaseQuickAdapter<NovelBook, BaseViewHolder> {


    private int position = -1;

    public BookAdapter(int layoutResId, @Nullable List<NovelBook> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NovelBook item) {


        helper.setText(R.id.book_tv_title, item.getBook_name_cn());


        if (helper.getAdapterPosition() == position) {

            helper.setBackgroundRes(R.id.book_ll_view, R.drawable.novel_shape_book_bg);
        } else {

            helper.setBackgroundRes(R.id.book_ll_view, R.drawable.novel_shape_book_bg_default);
        }

        ImageView book_iv_pic = helper.getView(R.id.book_iv_pic);

        if (NovelConstant.VIP_STATE == 0) {

            Glide.with(NovelApplication.getContext()).load(NovelConstant.STATIC2_URL + item.getPic()).into(book_iv_pic);
        } else {

            Glide.with(book_iv_pic.getContext()).load(NovelConstant.STATICVIP2_URL + item.getPic()).into(book_iv_pic);
        }

    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
