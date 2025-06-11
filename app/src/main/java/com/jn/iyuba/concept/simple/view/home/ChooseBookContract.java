package com.jn.iyuba.concept.simple.view.home;

import android.os.strictmode.Violation;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.BookBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ChooseBookContract {

    interface ChooseBookView extends LoadingView {

        void getTitleBySeries(BookBean bookBean);
    }

    interface ChooseBookPresenter extends IBasePresenter<ChooseBookView> {


        void getTitleBySeries(String type, int category
                , int appid, int uid, String sign);
    }

    interface ChooseBookModel extends BaseModel {


        Disposable getTitleBySeries(String type, int category
                , int appid, int uid, String sign, Callback callback);
    }

    interface Callback {

        void success(BookBean bookBean);

        void error(Exception e);
    }
}
