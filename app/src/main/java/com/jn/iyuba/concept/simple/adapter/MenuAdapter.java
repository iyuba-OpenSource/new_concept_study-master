package com.jn.iyuba.concept.simple.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.entity.SideNav;

import java.util.List;

public class MenuAdapter extends BaseQuickAdapter<SideNav, BaseViewHolder> {

    public MenuAdapter(int layoutResId, @Nullable List<SideNav> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SideNav item) {


        helper.setText(R.id.book_tv_title, item.getName());
    }
}
