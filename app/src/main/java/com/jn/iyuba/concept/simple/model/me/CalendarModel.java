package com.jn.iyuba.concept.simple.model.me;


import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.ClockInLogBean;
import com.jn.iyuba.concept.simple.view.me.CalendarContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CalendarModel implements CalendarContract.CalendarModel {


    @Override
    public Disposable getShareInfoShow(String uid, int appId, String time, CalendarContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getShareInfoShow(Constant.GET_SHARE_INFO_SHOW,uid, appId, time)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ClockInLogBean>() {
                    @Override
                    public void accept(ClockInLogBean clockInLogBean) throws Exception {

                        callback.success(clockInLogBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });

    }
}
