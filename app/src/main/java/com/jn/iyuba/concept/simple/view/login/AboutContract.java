package com.jn.iyuba.concept.simple.view.login;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.LogoffBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface AboutContract {


    interface AboutView extends LoadingView {

        void logoffComplete(LogoffBean logoffBean);
    }

    interface AboutPresenter extends IBasePresenter<AboutView> {

        void logoff(int protocol, String username, String password,
                    String format, String sign);
    }


    interface AboutModel extends BaseModel {

        Disposable logoff(int protocol, String username, String password,
                          String format, String sign, LogoffCallback logoffCallback);
    }


    interface LogoffCallback {

        void success(LogoffBean logoffBean);

        void error(Exception e);
    }
}
