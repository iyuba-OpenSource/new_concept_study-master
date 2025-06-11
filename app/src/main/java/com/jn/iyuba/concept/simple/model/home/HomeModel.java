package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.TitleBean;
import com.jn.iyuba.concept.simple.view.home.HomeContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeModel implements HomeContract.HomeModel {


    @Override
    public Disposable getConceptTitle(String language, int book, int flg, HomeContract.TitleCallback titleCallback) {

        return NetWorkManager
                .getRequest()
                .getConceptTitle(language, book, flg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TitleBean>() {
                    @Override
                    public void accept(TitleBean titleBean) throws Exception {

                        titleCallback.success(titleBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        titleCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTitleBySeriesid(String type, int appid, int uid, String sign, int seriesid, HomeContract.TitleCallback titleCallback) {

        return NetWorkManager
                .getRequest()
                .getTitleBySeriesid(Constant.GET_TITLE_BY_SERIES, type, appid, uid, sign, seriesid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TitleBean>() {
                    @Override
                    public void accept(TitleBean titleBean) throws Exception {

                        titleCallback.success(titleBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        titleCallback.error((Exception) throwable);
                    }
                });
    }
}
