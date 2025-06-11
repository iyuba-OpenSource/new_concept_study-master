package com.jn.iyuba.concept.simple.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Title;

import java.util.List;

public class IndicatorAdapter extends BaseQuickAdapter<Title, BaseViewHolder> {


    private int position = 0;

    public IndicatorAdapter(int layoutResId, @Nullable List<Title> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Title item) {

        ImageView indicator_iv = helper.getView(R.id.indicator_iv);

        if (position == helper.getAdapterPosition()) {

            indicator_iv.setImageResource(R.drawable.indicator_color);
        } else {

            indicator_iv.setImageResource(R.drawable.indicator_gray);
        }
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
