package com.jn.iyuba.concept.simple.util.popup;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.MoreAdapter;
import com.jn.iyuba.succinct.databinding.PopupMoreBinding;
import com.jn.iyuba.succinct.databinding.PopupPrivacyBinding;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class MorePopup extends BasePopupWindow {


    private PopupMoreBinding binding;

    private MoreAdapter moreAdapter;

    private Callback callback;

    public MorePopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_more);
        binding = PopupMoreBinding.bind(view);
        setContentView(view);

        moreAdapter = new MoreAdapter(R.layout.item_more, new ArrayList<>());
        binding.moreRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.moreRv.setAdapter(moreAdapter);

        moreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (callback != null) {

                    String str = moreAdapter.getItem(position);
                    callback.getItem(str);
                }
            }
        });
    }


    public void setDatas(List<String> strings) {

        moreAdapter.setNewData(strings);

    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {


        void getItem(String s);
    }


}
