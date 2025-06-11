package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.PointReadingBean;
import com.jn.iyuba.concept.simple.view.home.PointReadingContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PointReadingModel implements PointReadingContract.PointReadingModel {

    @Override
    public Disposable textExamApi(String format, String voaid, PointReadingContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .textExamApi(Constant.POINT_READING, format, voaid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PointReadingBean>() {
                    @Override
                    public void accept(PointReadingBean pointReadingBean) throws Exception {

                        callback.success(pointReadingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
