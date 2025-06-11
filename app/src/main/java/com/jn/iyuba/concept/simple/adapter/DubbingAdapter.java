package com.jn.iyuba.concept.simple.adapter;

import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Sentence;

import java.text.DecimalFormat;
import java.util.List;


public class DubbingAdapter extends BaseQuickAdapter<Sentence, BaseViewHolder> {

    private DecimalFormat decimalFormat;

    private int postion = -1;

    public DubbingAdapter(int layoutResId, @Nullable List<Sentence> data) {
        super(layoutResId, data);

        decimalFormat = new DecimalFormat("#0.0");
    }


    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Sentence item) {


        //index
        helper.setText(R.id.dubbing_tv_index, item.getParaid() + "/" + getItemCount());
        //句子
        helper.setText(R.id.dubbing_tv_sentence, item.getSentence());
        //句子 中文
        helper.setText(R.id.dubbing_tv_sentence_ch, item.getSentenceCn());
        //正常时间
        double duration = Double.parseDouble(item.getEndTiming()) - Double.parseDouble(item.getTiming());
        String durationStr = decimalFormat.format(duration);
        helper.setText(R.id.dubbing_tv_duration, durationStr + "s");
        helper.setText(R.id.dubbing_tv_duration2, durationStr + "s");
        //可超出的时间
        double overDuration = 0;
        String maxDurationStr = "1.0";
        int next = helper.getBindingAdapterPosition() + 1;
        if (next < getItemCount()) {//下一个位置是否有数据

            overDuration = Double.parseDouble(getItem(next).getTiming()) - Double.parseDouble(item.getEndTiming());
            maxDurationStr = decimalFormat.format(overDuration);
        }
        helper.setText(R.id.dubbing_tv_m_duration, maxDurationStr + "s");
        helper.addOnClickListener(R.id.dubbing_iv_record);
        helper.addOnClickListener(R.id.dubbing_iv_play);

        //播放的进度
        ProgressBar dubbing_pb_duration = helper.getView(R.id.dubbing_pb_duration);
        ProgressBar dubbing_pb_over_duration = helper.getView(R.id.dubbing_pb_over_duration);
        dubbing_pb_duration.setMax((int) (duration * 1000));
        dubbing_pb_over_duration.setMax((int) (overDuration * 1000));
        if (item.getRecordPosition() < (duration * 1000)) {

            dubbing_pb_duration.setProgress((int) item.getRecordPosition());
            dubbing_pb_over_duration.setProgress(0);
        } else {

            dubbing_pb_duration.setProgress((int) (duration * 1000));
            dubbing_pb_over_duration.setProgress((int) (item.getRecordPosition() - (duration * 1000)));
        }
        //播放按钮
        if (item.isPlaying()) {

            helper.setImageResource(R.id.dubbing_iv_play, R.mipmap.icon_eval_pause);
        } else {

            helper.setImageResource(R.id.dubbing_iv_play, R.mipmap.icon_eval_play);
        }


        //评测过

        if (item.getRecordSoundUrl() == null) {

            helper.setGone(R.id.dubbing_tv_score, false);
            helper.setVisible(R.id.dubbing_iv_play, false);
        } else {

            helper.setGone(R.id.dubbing_tv_score, true);
            helper.setVisible(R.id.dubbing_iv_play, true);

            double score = Double.parseDouble(item.getScore()) * 20;
            if (score < 45) {

                helper.setText(R.id.dubbing_tv_score, "");
                helper.setBackgroundRes(R.id.dubbing_tv_score, R.drawable.scroe_low);
            } else {

                helper.setText(R.id.dubbing_tv_score, score + "");
                helper.setBackgroundRes(R.id.dubbing_tv_score, R.drawable.oval_score);
            }

        }
    }
}
