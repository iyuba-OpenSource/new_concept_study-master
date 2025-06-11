package com.jn.iyuba.concept.simple.view.me;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.ClockInLogBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface CalendarContract {


    interface CalendarView extends LoadingView {

        void getLogComplete(ClockInLogBean clockInLogBean);
    }

    interface CalendarPresenter extends IBasePresenter<CalendarView> {

        void getShareInfoShow(String uid, int appId, String time);
    }

    interface CalendarModel extends BaseModel {

        Disposable getShareInfoShow(String uid, int appId, String time, Callback callback);
    }

    interface Callback {

        void success(ClockInLogBean clockInLogBean);

        void error(Exception e);
    }
}
