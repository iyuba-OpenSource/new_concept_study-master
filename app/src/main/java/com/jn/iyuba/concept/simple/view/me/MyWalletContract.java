package com.jn.iyuba.concept.simple.view.me;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.me.RewardBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MyWalletContract {

    interface MyWalletView extends LoadingView {


        void wallet(int pages, List<RewardBean.DataDTO> dataDTOS);
    }

    interface MyWalletPresenter extends IBasePresenter<MyWalletView> {

        void getUserActionRecord(int uid, int pages, int pageCount, String sign);
    }


    interface MyWalletModel extends BaseModel {

        Disposable getUserActionRecord(int uid, int pages, int pageCount, String sign, WalletCallback walletCallback);
    }

    interface WalletCallback {

        void success(RewardBean rewardBean);

        void error(Exception e);
    }
}
