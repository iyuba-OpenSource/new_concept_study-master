package com.jn.iyuba.concept.simple.model.me;


import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.view.me.MyWalletContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyWalletModel implements MyWalletContract.MyWalletModel {


    @Override
    public Disposable getUserActionRecord(int uid, int pages, int pageCount, String sign, MyWalletContract.WalletCallback walletCallback) {

        return NetWorkManager
                .getRequest()
                .getUserActionRecord(Constant.GET_USER_ACTION_RECORD, uid, pages, pageCount, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletCallback::success, throwable -> {

                    walletCallback.error((Exception) throwable);
                });
    }
}
