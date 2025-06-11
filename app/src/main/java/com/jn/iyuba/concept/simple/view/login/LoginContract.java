package com.jn.iyuba.concept.simple.view.login;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.UserBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface LoginContract {

    interface LoginView extends LoadingView {

        void loginComplete(UserBean.UserinfoDTO userBean);
    }


    interface LoginPresenter extends IBasePresenter<LoginView> {


        void login(int protocol, int appid, String username, String password, int x, int y, String sign);
    }

    interface LoginModel extends BaseModel {

        Disposable login(int protocol, int appid, String username, String password, int x,
                         int y, String sign, UserCallback userCallback);
    }

    interface UserCallback {

        void success(UserBean.UserinfoDTO userBean);

        void error(Exception e);
    }
}
