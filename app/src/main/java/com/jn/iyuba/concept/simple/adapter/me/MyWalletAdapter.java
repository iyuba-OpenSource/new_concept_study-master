package com.jn.iyuba.concept.simple.adapter.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.concept.simple.model.bean.me.RewardBean;
import com.jn.iyuba.succinct.R;

import java.text.DecimalFormat;
import java.util.List;


public class MyWalletAdapter extends BaseQuickAdapter<RewardBean.DataDTO, BaseViewHolder> {


    private DecimalFormat decimalFormat;

    public MyWalletAdapter(int layoutResId, @Nullable List<RewardBean.DataDTO> data) {
        super(layoutResId, data);
        decimalFormat = new DecimalFormat("#.##");
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RewardBean.DataDTO item) {

        helper.setText(R.id.mywallet_tv_type, item.getType());

        float s = Integer.parseInt(item.getScore()) / 100.0f;
        helper.setText(R.id.mywallet_tv_money, decimalFormat.format(s) + "元");

        helper.setText(R.id.mywallet_tv_date, item.getTime());
    }
}
