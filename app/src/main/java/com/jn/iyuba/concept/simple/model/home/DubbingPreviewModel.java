package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.entity.DubbingPublish;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.DubbingPublishBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateScoreBean;
import com.jn.iyuba.concept.simple.view.home.DubbingPreviewContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DubbingPreviewModel implements DubbingPreviewContract.DubbingPreviewModel {


    @Override
    public Disposable publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish,
                                     DubbingPreviewContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .publishDubbing(Constant.PUBLISH_DUBBING, protocol, content, userid, dubbingPublish)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DubbingPublishBean>() {
                    @Override
                    public void accept(DubbingPublishBean dubbingPublishBean) throws Exception {

                        callback.success(dubbingPublishBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile, DubbingPreviewContract.UpdateScoreCallback updateScoreCallback) {

        return NetWorkManager
                .getRequest()
                .updateScore(Constant.UPDATE_SCORE, srid, mobile, flag, uid, Integer.parseInt(appid))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ScoreBean>() {
                    @Override
                    public void accept(ScoreBean scoreBean) throws Exception {

                        updateScoreCallback.success(scoreBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        updateScoreCallback.error((Exception) throwable);
                    }
                });
    }
}
