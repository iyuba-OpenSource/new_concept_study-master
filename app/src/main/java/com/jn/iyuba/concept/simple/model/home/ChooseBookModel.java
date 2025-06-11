package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.BookBean;
import com.jn.iyuba.concept.simple.view.home.ChooseBookContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChooseBookModel implements ChooseBookContract.ChooseBookModel {


    @Override
    public Disposable getTitleBySeries(String type, int category, int appid, int uid, String sign, ChooseBookContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getTitleBySeries(Constant.GET_TITLE_BY_SERIES, type, category, appid, uid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookBean>() {
                    @Override
                    public void accept(BookBean bookBean) throws Exception {

                        callback.success(bookBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
