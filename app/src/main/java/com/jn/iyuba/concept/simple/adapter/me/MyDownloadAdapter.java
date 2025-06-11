package com.jn.iyuba.concept.simple.adapter.me;

import android.os.Environment;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Title;

import java.util.List;


public class MyDownloadAdapter extends BaseQuickAdapter<Title, BaseViewHolder> {


    public MyDownloadAdapter(int layoutResId, @Nullable List<Title> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Title item) {


        ImageView md_iv_img = helper.getView(R.id.md_iv_img);
        if (item.getPic() != null && !item.getPic().equals("")) {

            Glide.with(md_iv_img.getContext()).load(item.getPic()).into(md_iv_img);
        } else {

            Glide.with(md_iv_img.getContext()).load(R.mipmap.icon_lesson_default).into(md_iv_img);
        }

        helper.setText(R.id.md_tv_num, "LESSON " + (Integer.parseInt(item.getVoaId()) - item.getBookid() * 1000));

        helper.setText(R.id.md_tv_title, item.getTitle());

        helper.setText(R.id.md_tv_title_cn, item.getTitleCn());
    }
}
