package com.jn.iyuba.concept.simple.presenter.home;


import com.jn.iyuba.concept.simple.model.bean.RankingDetailsBean;
import com.jn.iyuba.concept.simple.model.home.RankingDetailsModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.RankingDetailsContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class RankingDetailsPresenter extends BasePresenter<RankingDetailsContract.RankingDetailsView, RankingDetailsContract.RankingDetailsModel>
        implements RankingDetailsContract.RankingDetailsPresenter {


    @Override
    protected RankingDetailsContract.RankingDetailsModel initModel() {
        return new RankingDetailsModel();
    }

    @Override
    public void getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign) {

        Disposable disposable = model.getWorksByUserId(uid, topic, topicId, shuoshuoType, sign, new RankingDetailsContract.Callback() {

            @Override
            public void success(RankingDetailsBean rankingDetailsBean) {

                if (rankingDetailsBean.isResult()) {

                    view.getMergeData(rankingDetailsBean);
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
