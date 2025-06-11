package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.DubbingRankBean;
import com.jn.iyuba.concept.simple.view.home.VideoRankContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VideoRankModel implements VideoRankContract.VideoRankMdoel {


    @Override
    public Disposable getDubbingRank(String platform, String format, int protocol, String voaid, int pageNumber,
                                     int pageCounts, int sort, String topic, int selectType, VideoRankContract.Callback callback) {


        return NetWorkManager
                .getRequest()
                .getDubbingRank(Constant.PUBLISH, platform, format, protocol, voaid, pageNumber, pageCounts, sort, topic, selectType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DubbingRankBean>() {
                    @Override
                    public void accept(DubbingRankBean dubbingRankBean) throws Exception {

                        callback.success(dubbingRankBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
