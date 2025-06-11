package com.jn.iyuba.concept.simple.presenter.home;

import com.jn.iyuba.concept.simple.model.bean.TitleBean;
import com.jn.iyuba.concept.simple.model.home.HomeModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.HomeContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class HomePresenter extends BasePresenter<HomeContract.HomeView, HomeContract.HomeModel>
        implements HomeContract.HomePresenter {


    @Override
    protected HomeContract.HomeModel initModel() {
        return new HomeModel();
    }

    @Override
    public void getConceptTitle(String language, int book, int flg) {

        Disposable disposable = model.getConceptTitle(language, book, flg, new HomeContract.TitleCallback() {
            @Override
            public void success(TitleBean titleBean) {

                view.getConceptTitle(titleBean, book, language);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }

        });
        addSubscribe(disposable);
    }

    @Override
    public void getTitleBySeriesid(String type, int appid, int uid, String sign, int seriesid) {

        Disposable disposable = model.getTitleBySeriesid(type, appid, uid, sign, seriesid, new HomeContract.TitleCallback() {
            @Override
            public void success(TitleBean titleBean) {

                view.getConceptTitle(titleBean, seriesid, null);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
