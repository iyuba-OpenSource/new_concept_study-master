package com.jn.iyuba.concept.simple.adapter.word;

import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.concept.simple.entity.WordBook;
import com.jn.iyuba.succinct.R;

import java.util.List;

public class WordBookAdapter extends BaseQuickAdapter<WordBook, BaseViewHolder> {


    private int position = 0;

    public WordBookAdapter(int layoutResId, @Nullable List<WordBook> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WordBook item) {

        helper.addOnClickListener(R.id.wb_rb_name);
        if (position == helper.getBindingAdapterPosition()) {

            helper.setChecked(R.id.wb_rb_name, true);
        } else {

            helper.setChecked(R.id.wb_rb_name, false);
        }
        helper.setText(R.id.wb_rb_name, item.getName() + "(" + item.getNum() + "个单词)");

    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
