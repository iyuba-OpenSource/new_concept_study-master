package com.jn.iyuba.concept.simple.adapter;

import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.entity.Vip;

import java.util.List;

public class VipAdapter extends BaseQuickAdapter<Vip.VipKind, BaseViewHolder> {

    private int position = 0;

    public VipAdapter(int layoutResId, @Nullable List<Vip.VipKind> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Vip.VipKind item) {

        RadioButton vip_rb_title = helper.getView(R.id.vip_rb_title);
        if (helper.getAdapterPosition() == position) {

            vip_rb_title.setChecked(true);
        } else {
            vip_rb_title.setChecked(false);
        }
        helper.setText(R.id.vip_rb_title, item.getName());
        helper.setText(R.id.vip_tv_price, "￥" + item.getPrice());
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
