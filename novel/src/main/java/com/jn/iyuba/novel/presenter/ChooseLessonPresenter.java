package com.jn.iyuba.novel.presenter;

import com.jn.iyuba.novel.model.ChooseLessonModel;
import com.jn.iyuba.novel.model.bean.NovelBookBean;
import com.jn.iyuba.novel.view.ChooseLessonContract;

import io.reactivex.disposables.Disposable;

public class ChooseLessonPresenter extends BasePresenter<ChooseLessonContract.ChooseLessonView, ChooseLessonContract.ChooseLessonModel>
        implements ChooseLessonContract.ChooseLessonPresenter {


    @Override
    protected ChooseLessonContract.ChooseLessonModel initModel() {
        return new ChooseLessonModel();
    }

    @Override
    public void getStroryInfo(String types, int level, String from) {

        Disposable disposable = model.getStroryInfo(types, level, from, new ChooseLessonContract.BookCallback() {
            @Override
            public void success(NovelBookBean bookBeanz) {

                if (bookBeanz.getResult() == 200) {

                    view.getBook(bookBeanz, from);
                } else {

                    view.getBook(null, null);
                }

            }

            @Override
            public void error(Exception e) {

                view.getBook(null, null);
            }
        });
        addSubscribe(disposable);
    }
}
