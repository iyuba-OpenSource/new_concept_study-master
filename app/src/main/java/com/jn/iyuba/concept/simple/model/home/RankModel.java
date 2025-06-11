package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.AudioRankingBean;
import com.jn.iyuba.concept.simple.view.home.RankingContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RankModel implements RankingContract.RankingModel {
    @Override
    public Disposable getTopicRanking(String topic, int topicid, int uid, String type, int start, int total, String sign, RankingContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getTopicRanking(Constant.GET_TOPIC_RANKING, topic, topicid, uid, type, start, total, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AudioRankingBean>() {
                    @Override
                    public void accept(AudioRankingBean audioRankingBean) throws Exception {

                        callback.success(audioRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
