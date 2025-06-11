package com.jn.iyuba.concept.simple.util.popup;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.adapter.word.WordBookAdapter;
import com.jn.iyuba.concept.simple.db.Book;
import com.jn.iyuba.concept.simple.entity.WordBook;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.PopupWordBookBinding;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class WordBookPopup extends BasePopupWindow {

    private PopupWordBookBinding binding;
    private WordBookAdapter wordBookAdapter;

    private Callback callback;

    public WordBookPopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_word_book);
        binding = PopupWordBookBinding.bind(view);
        setContentView(binding.getRoot());

        wordBookAdapter = new WordBookAdapter(R.layout.item_word_book, new ArrayList<>());
        binding.wbRvBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.wbRvBooks.setAdapter(wordBookAdapter);

        wordBookAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                wordBookAdapter.setPosition(position);
                wordBookAdapter.notifyDataSetChanged();
            }
        });

        binding.wbTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        binding.wbTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callback != null) {

                    callback.getBook(wordBookAdapter.getItem(wordBookAdapter.getPosition()));
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

    public void setDatas(List<WordBook> bookList) {

        wordBookAdapter.setNewData(bookList);
    }

    public interface Callback {

        void getBook(WordBook wordBook);
    }

}
