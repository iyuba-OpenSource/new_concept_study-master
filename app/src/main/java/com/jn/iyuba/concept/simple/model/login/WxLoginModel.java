package com.jn.iyuba.concept.simple.model.login;



import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.WxLoginBean;
import com.jn.iyuba.concept.simple.view.login.WxLoginContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WxLoginModel implements WxLoginContract.WxLoginModel {

    @Override
    public Disposable getWxAppletToken(String platform, String format, String protocol, String appid, String sign,
                                       WxLoginContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getWxAppletToken(Constant.PROTOCOL, platform, format, protocol, appid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WxLoginBean>() {
                    @Override
                    public void accept(WxLoginBean wxLoginBean) throws Exception {

                        callback.success(wxLoginBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getUidByToken(String platform, String format, String protocol, String token, String sign, String appid, WxLoginContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getUidByToken(Constant.PROTOCOL, platform, format, protocol, token, sign, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WxLoginBean>() {
                    @Override
                    public void accept(WxLoginBean wxLoginBean) throws Exception {

                        callback.success(wxLoginBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign, WxLoginContract.UserCallback callback) {

        return NetWorkManager
                .getRequest()
                .getMoreInfo(Constant.PROTOCOL,platform, protocol, id, myid, appid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoreInfoBean>() {
                    @Override
                    public void accept(MoreInfoBean moreInfoBean) throws Exception {

                        callback.success(moreInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

}
