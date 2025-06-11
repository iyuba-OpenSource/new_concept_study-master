package com.jn.iyuba.concept.simple.presenter.me;


import com.jn.iyuba.concept.simple.model.bean.ClockInLogBean;
import com.jn.iyuba.concept.simple.model.me.CalendarModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.me.CalendarContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class CalendarPresenter extends BasePresenter<CalendarContract.CalendarView, CalendarContract.CalendarModel>
        implements CalendarContract.CalendarPresenter {


    @Override
    protected CalendarContract.CalendarModel initModel() {
        return new CalendarModel();
    }

    @Override
    public void getShareInfoShow(String uid, int appId, String time) {

        Disposable disposable = model.getShareInfoShow(uid, appId, time, new CalendarContract.Callback() {
            @Override
            public void success(ClockInLogBean clockInLogBean) {

                if (clockInLogBean.getResult() == 200) {

                    view.getLogComplete(clockInLogBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
                if (e instanceof RuntimeException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
