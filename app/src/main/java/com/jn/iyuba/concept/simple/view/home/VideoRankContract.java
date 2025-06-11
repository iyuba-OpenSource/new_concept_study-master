package com.jn.iyuba.concept.simple.view.home;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.DubbingRankBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface VideoRankContract {

    interface VideoRankView extends LoadingView {

        void getDubbingRank(DubbingRankBean dubbingRankBean);
    }

    interface VideoRankPresenter extends IBasePresenter<VideoRankView> {

        void getDubbingRank(String platform, String format, int protocol, String voaid,
                            int pageNumber, int pageCounts, int sort, String topic,
                            int selectType);
    }

    interface VideoRankMdoel extends BaseModel {


        Disposable getDubbingRank(String platform, String format,
                                  int protocol, String voaid,
                                  int pageNumber, int pageCounts,
                                  int sort, String topic,
                                  int selectType, Callback callback);
    }


    interface Callback {

        void success(DubbingRankBean dubbingRankBean);

        void error(Exception e);
    }
}
