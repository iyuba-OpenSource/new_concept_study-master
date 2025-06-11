package com.jn.iyuba.concept.simple.util.popup;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.MenuAdapter;
import com.jn.iyuba.succinct.databinding.PopupBookBinding;
import com.jn.iyuba.concept.simple.entity.SideNav;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class BookPopup extends BasePopupWindow {


    private MenuAdapter menuAdapter;
    private PopupBookBinding popupBookBinding;

    private Callback callback;

    public BookPopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_book);
        popupBookBinding = PopupBookBinding.bind(view);
        setContentView(view);


        List<SideNav> sideNavList = new ArrayList<>();
        sideNavList.add(new SideNav(1, "新概念第一册"));
        sideNavList.add(new SideNav(2, "新概念第二册"));
        sideNavList.add(new SideNav(3, "新概念第三册"));
        sideNavList.add(new SideNav(4, "新概念第四册"));

        sideNavList.add(new SideNav(5, "关于我们"));
        sideNavList.add(new SideNav(6, "举报"));
        menuAdapter = new MenuAdapter(R.layout.item_book, sideNavList);
        popupBookBinding.bookRv.setLayoutManager(new LinearLayoutManager(getContext()));
        popupBookBinding.bookRv.setAdapter(menuAdapter);

        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                if (callback != null) {

                    SideNav sideNav = menuAdapter.getItem(position);
                    callback.getBook(sideNav);
                }

                dismiss();
            }
        });
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void getBook(SideNav sideNav);
    }
}
