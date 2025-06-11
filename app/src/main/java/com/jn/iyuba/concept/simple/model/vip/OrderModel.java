package com.jn.iyuba.concept.simple.model.vip;


import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.AlipayOrderBean;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.PayResultBean;
import com.jn.iyuba.concept.simple.model.bean.WXOrderBean;
import com.jn.iyuba.concept.simple.view.login.OrderContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class OrderModel implements OrderContract.OrderModel {
    @Override
    public Disposable alipayOrder(int app_id, int userId, String code, String WIDtotal_fee, int amount,
                                  int product_id, String WIDbody, String WIDsubject, OrderContract.Callback callback) {
        return NetWorkManager
                .getRequest()
                .alipayOrder(Constant.ALIPAY, app_id, userId, code, WIDtotal_fee, amount, product_id, WIDbody, WIDsubject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AlipayOrderBean>() {
                    @Override
                    public void accept(AlipayOrderBean alipayOrderBean) throws Exception {

                        callback.success(alipayOrderBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable notifyAliNew(String data, OrderContract.PayResultCallback payResultCallback) {

        return NetWorkManager
                .getRequest()
                .notifyAliNew(Constant.NOTIFY_ALI_NEW, data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PayResultBean>() {
                    @Override
                    public void accept(PayResultBean payResultBean) throws Exception {

                        payResultCallback.success(payResultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        payResultCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid,
                                  String sign, OrderContract.MoreInfoCallback moreInfoCallback) {

        return NetWorkManager
                .getRequest()
                .getMoreInfo(Constant.PROTOCOL, platform, protocol, id, myid, appid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoreInfoBean>() {
                    @Override
                    public void accept(MoreInfoBean moreInfoBean) throws Exception {

                        moreInfoCallback.success(moreInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        moreInfoCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable weixinPay(String wxkey, String appid, String weixinApp, String uid, String money,
                                String amount, String productid, String sign, String body, String format, OrderContract.WXCallback callback) {

        return NetWorkManager
                .getRequest()
                .weixinPay(Constant.WEIXINPAY, wxkey, appid, weixinApp, uid, money, amount, productid, sign, body, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WXOrderBean>() {
                    @Override
                    public void accept(WXOrderBean wxOrderBean) throws Exception {

                        callback.success(wxOrderBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
