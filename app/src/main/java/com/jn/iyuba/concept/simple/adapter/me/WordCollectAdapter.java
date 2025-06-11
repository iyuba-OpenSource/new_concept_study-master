package com.jn.iyuba.concept.simple.adapter.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordCollectListBean;
import com.jn.iyuba.succinct.R;

import java.util.List;


public class WordCollectAdapter extends BaseQuickAdapter<WordCollectListBean.DataDTO, BaseViewHolder> {


    public WordCollectAdapter(int layoutResId, @Nullable List<WordCollectListBean.DataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WordCollectListBean.DataDTO item) {

        helper.addOnClickListener(R.id.wc_iv_audio);
        helper.addOnClickListener(R.id.wc_tv_delete);

        helper.setText(R.id.wc_tv_word, item.getWord());
        helper.setText(R.id.wc_tv_pron, "[" + item.getPron() + "]");

        helper.setText(R.id.wc_tv_content, item.getDef());
    }
}
