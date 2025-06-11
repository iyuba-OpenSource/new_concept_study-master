package com.jn.iyuba.concept.simple.adapter.break_through;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.ConceptWord;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class CPWordAdapter extends BaseQuickAdapter<ConceptWord, BaseViewHolder> {

    public CPWordAdapter(int layoutResId, @Nullable List<ConceptWord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ConceptWord item) {

        helper.addOnClickListener(R.id.word_iv_word);
        helper.addOnClickListener(R.id.word_iv_sentence);

        //英文
        helper.setText(R.id.cpword_tv_word, item.getWord());
        if (item.getPron() == null) {

            helper.setText(R.id.cpword_tv_pron, "");
        } else {

            try {
                helper.setText(R.id.cpword_tv_pron, "[" + URLDecoder.decode(item.getPron(), "utf-8") + "]");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        helper.setText(R.id.word_tv_sentence, item.getSentence());


        //中文
        helper.setText(R.id.cpword_tv_wordch, item.getDef());
        helper.setText(R.id.word_tv_sentence_ch, item.getSentenceCn());

        int aStatus = item.getAnswer_status();
        if (aStatus == 0) {

            helper.setTextColor(R.id.cpword_tv_word, Color.BLACK);
            helper.setTextColor(R.id.cpword_tv_wordch, Color.BLACK);
            helper.setTextColor(R.id.cpword_tv_pron, Color.BLACK);

        } else if (aStatus == 1) {

            int gColor = mContext.getResources().getColor(R.color.mColorPrimary);
            helper.setTextColor(R.id.cpword_tv_word, gColor);
            helper.setTextColor(R.id.cpword_tv_wordch, gColor);
            helper.setTextColor(R.id.cpword_tv_pron, gColor);
        } else {

            int rColor = Color.parseColor("#E61A1A");
            helper.setTextColor(R.id.cpword_tv_word, rColor);
            helper.setTextColor(R.id.cpword_tv_wordch, rColor);
            helper.setTextColor(R.id.cpword_tv_pron, rColor);
        }
    }
}
