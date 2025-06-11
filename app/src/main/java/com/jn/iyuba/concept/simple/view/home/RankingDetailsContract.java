package com.jn.iyuba.concept.simple.view.home;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.RankingDetailsBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface RankingDetailsContract {


    interface RankingDetailsView extends LoadingView {


        void getMergeData(RankingDetailsBean rankingDetailsBean);
    }

    interface RankingDetailsPresenter extends IBasePresenter<RankingDetailsView> {

        void getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign);
    }

    interface RankingDetailsModel extends BaseModel {


        Disposable getWorksByUserId(int uid, String topic, int topicId, String shuoshuoType, String sign, Callback callback);
    }

    interface Callback {

        void success(RankingDetailsBean rankingDetailsBean);

        void error(Exception e);
    }
}
