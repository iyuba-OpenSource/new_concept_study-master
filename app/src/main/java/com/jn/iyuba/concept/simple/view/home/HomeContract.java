package com.jn.iyuba.concept.simple.view.home;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.TitleBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface HomeContract {


    interface HomeView extends LoadingView {


        void getConceptTitle(TitleBean titleBean, int bookid, String language);
    }

    interface HomePresenter extends IBasePresenter<HomeView> {

        void getConceptTitle(String language, int book, int flg);

        void getTitleBySeriesid(String type, int appid, int uid, String sign, int seriesid);
    }

    interface HomeModel extends BaseModel {

        Disposable getConceptTitle(String language, int book, int flg, TitleCallback titleCallback);

        Disposable getTitleBySeriesid(String type, int appid
                , int uid, String sign, int seriesid, TitleCallback titleCallback);
    }


    interface TitleCallback {

        void success(TitleBean titleBean);

        void error(Exception e);
    }
}
