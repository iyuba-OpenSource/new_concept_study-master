package com.jn.iyuba.novel.view;

import android.view.View;

import com.jn.iyuba.novel.model.BaseModel;
import com.jn.iyuba.novel.model.bean.ChapterBean;
import com.jn.iyuba.novel.presenter.IBasePresenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface ChapterContract {

    interface ChapterView extends LoadingView {

        /**
         *
         * @param chapterBean
         * @param from    对应的是
         */
        void getStroryInfo(ChapterBean chapterBean,String from);
    }

    interface ChapterPresenter extends IBasePresenter<ChapterView> {

        void getStroryInfo(String types, int level, int orderNumber, String from);
    }

    interface ChapterModel extends BaseModel {


        Disposable getStroryInfo(String types, int level, int orderNumber, String from, Callback callback);
    }

    interface Callback {

        void success(ChapterBean chapterBean);

        void error(Exception e);
    }
}
