package com.jn.iyuba.concept.simple.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.entity.Para;

import java.util.List;

public class ParaAdapter extends BaseQuickAdapter<Para, BaseViewHolder> {


    public ParaAdapter(int layoutResId, @Nullable List<Para> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Para item) {

        helper.setText(R.id.para_tv_en, item.getEn());
        helper.setText(R.id.para_tv_cn, item.getCn());
    }
}
