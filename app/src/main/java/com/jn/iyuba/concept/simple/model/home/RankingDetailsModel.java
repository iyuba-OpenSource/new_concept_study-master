package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.RankingDetailsBean;
import com.jn.iyuba.concept.simple.view.home.RankingDetailsContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RankingDetailsModel implements RankingDetailsContract.RankingDetailsModel {


    @Override
    public Disposable getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign, RankingDetailsContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getWorksByUserId(Constant.GET_WORKS_BY_USER_ID,uid, topic, topicId, shuoshuoType, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RankingDetailsBean>() {
                    @Override
                    public void accept(RankingDetailsBean rankingDetailsBean) throws Exception {

                        callback.success(rankingDetailsBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
