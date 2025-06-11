package com.jn.iyuba.concept.simple.view.home;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.PointReadingBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface PointReadingContract {

    interface PointReadingView extends LoadingView {


        void textExamApi(PointReadingBean pointReadingBean);
    }

    interface PointReadingPresenter extends IBasePresenter<PointReadingView> {

        void textExamApi(String format, String voaid);
    }

    interface PointReadingModel extends BaseModel {

        Disposable textExamApi(String format, String voaid, Callback callback);
    }

    interface Callback {

        void success(PointReadingBean pointReadingBean);

        void error(Exception e);
    }

}
