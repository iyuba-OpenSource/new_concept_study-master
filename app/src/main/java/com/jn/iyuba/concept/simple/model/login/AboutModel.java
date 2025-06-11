package com.jn.iyuba.concept.simple.model.login;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.LogoffBean;
import com.jn.iyuba.concept.simple.view.login.AboutContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AboutModel implements AboutContract.AboutModel {


    @Override
    public Disposable logoff(int protocol, String username, String password, String format, String sign, AboutContract.LogoffCallback logoffCallback) {

        return NetWorkManager
                .getRequest()
                .logoff(Constant.PROTOCOL,protocol, username, password, format, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LogoffBean>() {
                    @Override
                    public void accept(LogoffBean logoffBean) throws Exception {

                        logoffCallback.success(logoffBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        logoffCallback.error((Exception) throwable);
                    }
                });
    }
}
