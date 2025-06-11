package com.jn.iyuba.concept.simple.adapter;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.jn.iyuba.concept.simple.model.bean.PointReadingBean;
import com.jn.iyuba.concept.simple.util.widget.PointReadingImageView;
import com.jn.iyuba.succinct.R;

import java.util.ArrayList;
import java.util.List;

public class PointReadingAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private PointReadingBean pointReadingBean;

    private Callback callback;

    public PointReadingAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        PointReadingImageView pr_iv_img = helper.getView(R.id.pr_iv_img);
        pr_iv_img.setMyClickListen(new PointReadingImageView.MyClickListen() {
            @Override
            public void get(PointReadingBean.VoatextDTO voatextDTO) {

                if (callback != null) {

                    callback.play(voatextDTO);
                }
            }
        });
        if (pointReadingBean != null) {//获取本图片的相关数据

            List<PointReadingBean.VoatextDTO> voatextDTOList = pointReadingBean.getVoatext();
            List<PointReadingBean.VoatextDTO> aboutList = new ArrayList<>();
            for (int i = 0; i < voatextDTOList.size(); i++) {

                PointReadingBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
                if (item.equals(voatextDTO.getImgPath())) {

                    aboutList.add(voatextDTO);
                }
            }
            pr_iv_img.setVoatextDTOList(aboutList);
        }
        Glide.with(pr_iv_img.getContext()).load("http://static.iyuba.cn/images/voa" + item).into(pr_iv_img);
    }


    public PointReadingBean getPointReadingBean() {
        return pointReadingBean;
    }

    public void setPointReadingBean(PointReadingBean pointReadingBean) {
        this.pointReadingBean = pointReadingBean;
    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void play(PointReadingBean.VoatextDTO voatextDTO);
    }
}
