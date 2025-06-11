package com.jn.iyuba.concept.simple.presenter.home;

import com.jn.iyuba.concept.simple.model.bean.DubbingRankBean;
import com.jn.iyuba.concept.simple.model.home.VideoRankModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.VideoRankContract;

import io.reactivex.disposables.Disposable;

public class VideoRankPresenter extends BasePresenter<VideoRankContract.VideoRankView, VideoRankContract.VideoRankMdoel>
        implements VideoRankContract.VideoRankPresenter {


    @Override
    protected VideoRankContract.VideoRankMdoel initModel() {
        return new VideoRankModel();
    }

    @Override
    public void getDubbingRank(String platform, String format, int protocol, String voaid, int pageNumber,
                               int pageCounts, int sort, String topic, int selectType) {

        Disposable disposable = model.getDubbingRank(platform, format, protocol, voaid, pageNumber,
                pageCounts, sort, topic, selectType, new VideoRankContract.Callback() {
                    @Override
                    public void success(DubbingRankBean dubbingRankBean) {

                        view.getDubbingRank(dubbingRankBean);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
        addSubscribe(disposable);
    }
}
