package com.jn.iyuba.concept.simple.view.me;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.me.SuggestBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface FeedbackContract {

    interface FeedbackView extends LoadingView {

        void suggestComplete();
    }


    interface FeedbackPresenter extends IBasePresenter<FeedbackView> {

        void suggest(int protocol, int uid, String content, String email,
                     String app, String platform);
    }

    interface FeedbackModel extends BaseModel {

        Disposable suggest(int protocol, int uid, String content, String email,
                           String app, String platform, SuggestCallback suggestCallback);
    }

    interface SuggestCallback {

        void success(SuggestBean suggestBean);

        void error(Exception e);
    }
}
