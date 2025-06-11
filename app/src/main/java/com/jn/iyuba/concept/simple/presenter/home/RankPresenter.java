package com.jn.iyuba.concept.simple.presenter.home;

import com.jn.iyuba.concept.simple.model.bean.AudioRankingBean;
import com.jn.iyuba.concept.simple.model.home.RankModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.RankingContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class RankPresenter extends BasePresenter<RankingContract.RankingView, RankingContract.RankingModel>
        implements RankingContract.RankingPresenter {


    @Override
    protected RankingContract.RankingModel initModel() {
        return new RankModel();
    }

    @Override
    public void getTopicRanking(String topic, int topicid, int uid, String type, int start, int total, String sign) {

        Disposable disposable = model.getTopicRanking(topic, topicid, uid, type, start, total, sign, new RankingContract.Callback() {
            @Override
            public void success(AudioRankingBean audioRankingBean) {

                if (audioRankingBean.getResult() != -1) {

                    view.getTopicRanking(audioRankingBean, start);
                }
            }

            @Override
            public void error(Exception e) {

                if(e instanceof UnknownHostException){


                }
            }
        });
        addSubscribe(disposable);
    }
}
