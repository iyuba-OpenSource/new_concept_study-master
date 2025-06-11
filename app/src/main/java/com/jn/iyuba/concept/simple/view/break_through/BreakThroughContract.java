package com.jn.iyuba.concept.simple.view.break_through;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.WordBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface BreakThroughContract {


    interface BreakThroughView extends LoadingView {

        void getConceptWord(WordBean wordBean, int bookid);
    }

    interface BreakThroughPresenter extends IBasePresenter<BreakThroughView> {


        void getConceptWord(String url, int book);

        void getWordByUnit(String bookid);
    }

    interface BreakThroughModel extends BaseModel {

        Disposable getConceptWord(String url, int book, WordCallback wordCallback);

        Disposable getWordByUnit(String bookid, WordCallback wordCallback);
    }


    interface WordCallback {

        void success(WordBean wordBean);

        void error(Exception e);
    }

}
