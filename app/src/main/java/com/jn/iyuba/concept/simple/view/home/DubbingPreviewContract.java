package com.jn.iyuba.concept.simple.view.home;


import com.jn.iyuba.concept.simple.entity.DubbingPublish;
import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.DubbingPublishBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateScoreBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface DubbingPreviewContract {

    interface DubbingPreviewView extends LoadingView {

        void publishDubbingCompelte(DubbingPublishBean dubbingPublishBean);

        void updateScore(ScoreBean scoreBean);
    }

    interface DubbingPreviewPresenter extends IBasePresenter<DubbingPreviewView> {


        void publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish);

        void updateScore(String flag, String uid, String appid
                , String idindex, int srid, int mobile);
    }

    interface DubbingPreviewModel extends BaseModel {


        Disposable publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish, Callback callback);

        Disposable updateScore(String flag, String uid, String appid
                , String idindex, int srid, int mobile, UpdateScoreCallback updateScoreCallback);
    }

    interface Callback {

        void success(DubbingPublishBean dubbingPublishBean);

        void error(Exception e);
    }


    interface UpdateScoreCallback {

        void success(ScoreBean scoreBean);

        void error(Exception e);
    }
}
