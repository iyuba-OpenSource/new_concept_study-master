package com.jn.iyuba.concept.simple.model;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.bean.ProcessBean;
import com.jn.iyuba.concept.simple.view.MainContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainModel implements MainContract.MainModel {


    @Override
    public Disposable getRegisterAll(String appId, String appVersion, MainContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getRegisterAll(appId, appVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProcessBean>() {
                    @Override
                    public void accept(ProcessBean processBean) throws Exception {

                        callback.success(processBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
