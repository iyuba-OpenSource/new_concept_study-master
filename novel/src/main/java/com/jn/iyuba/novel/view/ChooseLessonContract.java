package com.jn.iyuba.novel.view;

import com.jn.iyuba.novel.model.BaseModel;
import com.jn.iyuba.novel.model.bean.NovelBookBean;
import com.jn.iyuba.novel.presenter.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface ChooseLessonContract {

    interface ChooseLessonView extends LoadingView {

        void getBook(NovelBookBean novelBookBean, String from);
    }

    interface ChooseLessonPresenter extends IBasePresenter<ChooseLessonView> {

        void getStroryInfo(String types, int level, String from);
    }

    interface ChooseLessonModel extends BaseModel {


        Disposable getStroryInfo(String types, int level, String from, BookCallback callback);
    }

    interface BookCallback {

        void success(NovelBookBean novelBookBeanz);

        void error(Exception e);
    }
}
