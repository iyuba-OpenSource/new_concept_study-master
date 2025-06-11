package com.jn.iyuba.concept.simple.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.widget.rpb.RoundProgressBar;
import com.jn.iyuba.concept.simple.util.widget.SelectWordTextView;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.EvalWord;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;

import java.util.List;

public class EvaluatingAdapter extends BaseQuickAdapter<Sentence, BaseViewHolder> {

    private boolean isPlay = false;

    private boolean isRecord = false;

    //分贝
    private double db;

    /**
     * 正在操作的数据位置
     */
    private int position = -1;

    /**
     * 1:播放原音
     * 2:播放录音
     */
    private int flag = 1;

    private SelectWordTextView.OnClickWordListener onClickWordListener;


    public EvaluatingAdapter(int layoutResId, @Nullable List<Sentence> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Sentence sentence) {

        helper.addOnClickListener(R.id.evaluating_iv_play);
        helper.addOnClickListener(R.id.evaluating_rpb_record);
        helper.addOnClickListener(R.id.evaluating_iv_play_oneself);
        helper.addOnClickListener(R.id.evaluating_iv_submit);
        helper.addOnClickListener(R.id.evaluating_iv_share);
        //句子

        helper.setText(R.id.evaluating_tv_ch, sentence.getSentenceCn().trim());


        SelectWordTextView evaluating_tv_text = helper.getView(R.id.evaluating_tv_text);
        if (onClickWordListener != null) {

            evaluating_tv_text.setOnClickWordListener(onClickWordListener);
        }
        if (sentence.getWordsDTOS() != null) {

            evaluating_tv_text.setText(cSpannable(sentence.getSentence().trim(), sentence.getWordsDTOS()));
        } else {

            evaluating_tv_text.setText(sentence.getSentence());
        }


        //设置录音按钮
        RoundProgressBar evaluating_rpb_record = helper.getView(R.id.evaluating_rpb_record);
        if (isRecord && position == helper.getBindingAdapterPosition()) {

            evaluating_rpb_record.setProgress((int) db);
        } else {

            evaluating_rpb_record.setProgress(0);
        }
        ImageView evaluating_iv_play = helper.getView(R.id.evaluating_iv_play);
        ImageView evaluating_iv_play_oneself = helper.getView(R.id.evaluating_iv_play_oneself);
        //设置播放按钮的状态
        if (isPlay && position == helper.getBindingAdapterPosition()) {

            if (flag == 1) {//播放原音

                evaluating_iv_play.setImageResource(R.mipmap.icon_eval_pause);
            } else {//播放录音

                evaluating_iv_play_oneself.setImageResource(R.mipmap.icon_eval_pause_record);
            }

        } else {

            if (flag == 1) {//播放原音

                evaluating_iv_play.setImageResource(R.mipmap.icon_eval_play);
            } else {//播放录音

                evaluating_iv_play_oneself.setImageResource(R.mipmap.icon_eval_play_record);
            }
        }
        //得分
        if (sentence.getScore() == null) {

            helper.setGone(R.id.evaluating_tv_source, false);
            helper.setGone(R.id.evaluating_iv_play_oneself, false);
            helper.setGone(R.id.evaluating_iv_submit, false);
            helper.setGone(R.id.evaluating_iv_share, false);
        } else {

            helper.setGone(R.id.evaluating_tv_source, true);
            helper.setGone(R.id.evaluating_iv_play_oneself, true);
            helper.setGone(R.id.evaluating_iv_submit, true);

            helper.setText(R.id.evaluating_tv_source, (int) (Double.parseDouble(sentence.getScore()) * 20) + "");

            if (sentence.getTypes() == null) {//剑桥和书虫没有分享

                helper.setGone(R.id.evaluating_iv_share, true);
            } else {
                helper.setGone(R.id.evaluating_iv_share, false);
            }
        }
    }


    /**
     * 创建有颜色的句子
     *
     * @param Sentence
     * @param evalWordList
     * @return
     */
    private SpannableString cSpannable(String Sentence, List<EvalWord> evalWordList) {

        SpannableString spannableString = new SpannableString(Sentence);
        int index = 0;
        for (int i = 0; i < evalWordList.size(); i++) {

            EvalWord evalWord = evalWordList.get(i);
            double score = Double.parseDouble(evalWord.getScore());
            if (score < 2) {
                spannableString.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 0, 0)), index, index + evalWord.getContent().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else if (score > 3) {

                spannableString.setSpan(new ForegroundColorSpan(Color.argb(255, 6, 142, 0)), index, index + evalWord.getContent().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {

                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), index, index + evalWord.getContent().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            index = index + evalWord.getContent().length() + 1;
        }
        return spannableString;
    }


    public SelectWordTextView.OnClickWordListener getOnClickWordListener() {
        return onClickWordListener;
    }

    public void setOnClickWordListener(SelectWordTextView.OnClickWordListener onClickWordListener) {
        this.onClickWordListener = onClickWordListener;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }

    public double getDb() {
        return db;
    }

    public void setDb(double db) {
        this.db = db;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
