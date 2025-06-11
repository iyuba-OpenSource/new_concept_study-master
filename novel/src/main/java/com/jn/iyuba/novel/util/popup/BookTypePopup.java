package com.jn.iyuba.novel.util.popup;

import android.content.Context;
import android.view.View;

import com.contrarywind.listener.OnItemSelectedListener;
import com.jn.iyuba.novel.R;
import com.jn.iyuba.novel.adapter.BookTypeAdapter;
import com.jn.iyuba.novel.databinding.PopupBookTypeBinding;
import com.jn.iyuba.novel.entity.BookType;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class BookTypePopup extends BasePopupWindow {

    private PopupBookTypeBinding binding;

    private BookTypeAdapter bookTypeAdapter;

    private Callback callback;

    public BookTypePopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_book_type);
        binding = PopupBookTypeBinding.bind(view);
        setContentView(binding.getRoot());


        bookTypeAdapter = new BookTypeAdapter(new ArrayList<>());
        binding.wheelview.setCyclic(false);
        binding.wheelview.setLineSpacingMultiplier(2f);
        binding.wheelview.setAdapter(bookTypeAdapter);
        binding.wheelview.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {


            }
        });
        binding.btTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        binding.btTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookType bookType = bookTypeAdapter.getStringList().get(binding.wheelview.getCurrentItem());

                if (callback != null) {

                    callback.getType(bookType);
                }
            }
        });
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setData(List<BookType> bookTypeList) {

        bookTypeAdapter.setStringList(bookTypeList);
        binding.wheelview.invalidate();
    }

    public interface Callback {

        void getType(BookType type);

    }
}
