package com.jn.iyuba.concept.simple.model.login;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.UserBean;
import com.jn.iyuba.concept.simple.view.login.LoginContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginModel implements LoginContract.LoginModel {


    @Override
    public Disposable login(int protocol, int appid, String username, String password, int x, int y, String sign,
                            LoginContract.UserCallback userCallback) {

        return NetWorkManager
                .getRequest()
                .login(Constant.PROTOCOL, protocol, appid, username, password, x, y, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserBean.UserinfoDTO>() {
                    @Override
                    public void accept(UserBean.UserinfoDTO userinfoDTO) throws Exception {

                        userCallback.success(userinfoDTO);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        userCallback.error((Exception) throwable);
                    }
                });
    }
}
