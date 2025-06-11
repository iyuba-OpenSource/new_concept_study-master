package com.jn.iyuba.concept.simple.presenter.login;

import com.jn.iyuba.concept.simple.model.bean.UserBean;
import com.jn.iyuba.concept.simple.model.login.LoginModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.login.LoginContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginContract.LoginView, LoginContract.LoginModel>
        implements LoginContract.LoginPresenter {


    @Override
    protected LoginContract.LoginModel initModel() {
        return new LoginModel();
    }

    @Override
    public void login(int protocol, int appid, String username, String password, int x, int y, String sign) {

        Disposable disposable = model.login(protocol, appid, username, password, x, y, sign, new LoginContract.UserCallback() {
            @Override
            public void success(UserBean.UserinfoDTO userBean) {

                if (userBean != null && userBean.getResult().equals("101")) {

                    view.loginComplete(userBean);
                } else {

                    view.toast("账号或密码错误！");
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
