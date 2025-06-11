package com.jn.iyuba.concept.simple.presenter.me;


import com.jn.iyuba.concept.simple.model.bean.me.RewardBean;
import com.jn.iyuba.concept.simple.model.me.MyWalletModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.me.MyWalletContract;

import io.reactivex.disposables.Disposable;

public class MyWalletPresenter extends BasePresenter<MyWalletContract.MyWalletView, MyWalletContract.MyWalletModel>
        implements MyWalletContract.MyWalletPresenter {


    @Override
    protected MyWalletContract.MyWalletModel initModel() {
        return new MyWalletModel();
    }

    @Override
    public void getUserActionRecord(int uid, int pages, int pageCount, String sign) {

        Disposable disposable = model.getUserActionRecord(uid, pages, pageCount, sign, new MyWalletContract.WalletCallback() {

            @Override
            public void success(RewardBean rewardBean) {

                if (rewardBean.getResult() == 200) {

                    view.wallet(pages, rewardBean.getData());
                }
            }

            @Override
            public void error(Exception e) {

                view.wallet(pages, null);
            }
        });
        addSubscribe(disposable);
    }
}
