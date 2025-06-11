package com.jn.iyuba.concept.simple.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.concept.simple.db.ConceptWord;

import java.util.List;

public class ContentWordAdapter extends BaseQuickAdapter<ConceptWord, BaseViewHolder> {

    public ContentWordAdapter(int layoutResId, @Nullable List<ConceptWord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ConceptWord item) {

    }
}
