package com.jn.iyuba.concept.simple.model.vip;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean2;
import com.jn.iyuba.concept.simple.view.vip.VipContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VipModel implements VipContract.VipModel {


    @Override
    public Disposable getQQGroup(String type, VipContract.JpQQ2Callback callback) {

        return NetWorkManager
                .getRequest()
                .getQQGroup(Constant.GET_QQ_GROUP, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpQQBean2>() {
                    @Override
                    public void accept(JpQQBean2 jpQQBean2) throws Exception {

                        callback.success(jpQQBean2);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getJpQQ(int appid, VipContract.JpQQCallback callback) {

        return NetWorkManager
                .getRequest()
                .getJpQQ(Constant.GET_JP_QQ, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JpQQBean>() {
                    @Override
                    public void accept(JpQQBean jpQQBean) throws Exception {

                        callback.success(jpQQBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
