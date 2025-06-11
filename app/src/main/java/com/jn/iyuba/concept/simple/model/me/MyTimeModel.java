package com.jn.iyuba.concept.simple.model.me;


import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.MyTimeBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.view.me.MyTimeContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyTimeModel implements MyTimeContract.MyTimModel {


    @Override
    public Disposable getMyTime(String uid, int day, int flg, MyTimeContract.MyTimeCallback myTimeCallback) {

        return NetWorkManager
                .getRequest()
                .getMyTime(Constant.GET_MY_TIME, uid, day, flg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyTimeBean>() {
                    @Override
                    public void accept(MyTimeBean myTimeBean) throws Exception {

                        myTimeCallback.success(myTimeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        myTimeCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateScore(int srid, int mobile, String flag, String uid, int appid,
                                  MyTimeContract.ScoreCallback callback) {

        return NetWorkManager
                .getRequest()
                .updateScore(Constant.UPDATE_SCORE, srid, mobile, flag, uid, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ScoreBean>() {
                    @Override
                    public void accept(ScoreBean scoreBean) throws Exception {

                        callback.success(scoreBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });

    }
}
