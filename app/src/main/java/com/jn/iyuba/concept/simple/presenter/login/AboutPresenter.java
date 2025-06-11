package com.jn.iyuba.concept.simple.presenter.login;

import com.jn.iyuba.concept.simple.model.bean.LogoffBean;
import com.jn.iyuba.concept.simple.model.login.AboutModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.login.AboutContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class AboutPresenter extends BasePresenter<AboutContract.AboutView, AboutContract.AboutModel>
        implements AboutContract.AboutPresenter {


    @Override
    protected AboutContract.AboutModel initModel() {
        return new AboutModel();
    }


    @Override
    public void logoff(int protocol, String username, String password, String format, String sign) {

        Disposable disposable = model.logoff(protocol, username, password, format, sign, new AboutContract.LogoffCallback() {
            @Override
            public void success(LogoffBean logoffBean) {

                view.hideLoading();
                if (logoffBean.getResult().equals("101")) {

                    view.logoffComplete(logoffBean);
                    view.toast("注销成功");
                } else if (logoffBean.getResult().equals("103")) {

                    view.toast("密码输入错误");
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
