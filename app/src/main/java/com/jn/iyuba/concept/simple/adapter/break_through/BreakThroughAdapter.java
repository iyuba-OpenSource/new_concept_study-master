package com.jn.iyuba.concept.simple.adapter.break_through;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.CheckPoint;
import com.jn.iyuba.concept.simple.model.bean.TitleBean;

import java.util.List;

public class BreakThroughAdapter extends BaseQuickAdapter<CheckPoint, BaseViewHolder> {


    public BreakThroughAdapter(int layoutResId, @Nullable List<CheckPoint> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CheckPoint item) {


        helper.setText(R.id.bt_tv_checkpoint, "第" + (helper.getBindingAdapterPosition() + 1) + "关");
        if (item.isPass()) {

//            helper.setTextColor(R.id.bt_tv_checkpoint, mContext.getResources().getColor(R.color.colorPrimary));
            helper.setText(R.id.bt_tv_true, item.gettCount() + "/" + item.getTotal());
            helper.setBackgroundRes(R.id.bt_rl, R.drawable.shape_rctg_bg_bt_green);
            helper.setGone(R.id.bt_iv_status, false);
        } else {

//            helper.setTextColor(R.id.bt_tv_checkpoint, mContext.getResources().getColor(R.color.black));
            helper.setText(R.id.bt_tv_true, "未解锁");
            helper.setBackgroundRes(R.id.bt_rl, R.drawable.shape_rctg_bg_bt_gray);

            helper.setGone(R.id.bt_iv_status, true);

            helper.setGone(R.id.bt_tv_true, true);
        }
    }
}
