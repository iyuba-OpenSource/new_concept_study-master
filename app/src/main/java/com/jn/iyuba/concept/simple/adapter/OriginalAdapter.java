package com.jn.iyuba.concept.simple.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.util.widget.SelectWordTextView;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;

import java.util.List;

/**
 * 原文fragment
 */
public class OriginalAdapter extends BaseQuickAdapter<Sentence, BaseViewHolder> {

    private int position = 0;

    private int gColor;

    private int blackColor = 0;

    private int grayColor = 0;

    private boolean isShowEnCn = true;

    private Callback callback;


    public OriginalAdapter(int layoutResId, @Nullable List<Sentence> data) {
        super(layoutResId, data);
        gColor = Color.parseColor("#92CB7E");
        blackColor = Color.GRAY;
        grayColor = Color.GRAY;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Sentence item) {


        if (isShowEnCn) {

            helper.setGone(R.id.original_tv_ch, true);
        } else {

            helper.setGone(R.id.original_tv_ch, false);
        }

        SelectWordTextView original_tv_en = helper.getView(R.id.original_tv_en);
        original_tv_en.clearText();
        original_tv_en.setText(item.getSentence());

        original_tv_en.setOnClickWordListener(new SelectWordTextView.OnClickWordListener() {
            @Override
            public void onClickWord(String word) {

                if (callback != null) {

                    callback.getWord(word, helper.getBindingAdapterPosition());
                }
            }
        });


        helper.setText(R.id.original_tv_ch, item.getSentenceCn());

        if (helper.getBindingAdapterPosition() == position) {//当前在读的位置

            helper.setTextColor(R.id.original_tv_en, gColor);

            helper.setTextColor(R.id.original_tv_ch, gColor);
        } else {

            helper.setTextColor(R.id.original_tv_en, blackColor);

            helper.setTextColor(R.id.original_tv_ch, grayColor);
        }
    }


    public boolean isShowEnCn() {
        return isShowEnCn;
    }

    public void setShowEnCn(boolean showEnCn) {
        isShowEnCn = showEnCn;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void getWord(String word, int position);
    }
}
