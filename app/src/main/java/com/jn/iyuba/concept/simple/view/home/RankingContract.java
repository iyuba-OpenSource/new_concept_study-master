package com.jn.iyuba.concept.simple.view.home;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.AudioRankingBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface RankingContract {

    interface RankingView extends LoadingView {


        void getTopicRanking(AudioRankingBean audioRankingBean, int start);
    }

    interface RankingPresenter extends IBasePresenter<RankingView> {

        void getTopicRanking(String topic, int topicid, int uid, String type,
                             int start, int total, String sign);
    }

    interface RankingModel extends BaseModel {

        Disposable getTopicRanking(String topic, int topicid, int uid, String type,
                                   int start, int total, String sign, Callback callback);
    }

    interface Callback {

        void success(AudioRankingBean audioRankingBean);

        void error(Exception e);
    }
}
