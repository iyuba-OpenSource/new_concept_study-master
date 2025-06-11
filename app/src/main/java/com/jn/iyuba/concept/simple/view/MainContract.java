package com.jn.iyuba.concept.simple.view;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.ProcessBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface MainContract {


    interface MainView extends LoadingView {


        void getRegisterAll(ProcessBean processBean);
    }

    interface MainPresenter extends IBasePresenter<MainView> {

        void getRegisterAll(String appId, String appVersion);
    }

    interface MainModel extends BaseModel {

        Disposable getRegisterAll(String appId, String appVersion, Callback callback);
    }

    interface Callback {

        void success(ProcessBean processBean);

        void error(Exception e);
    }
}

