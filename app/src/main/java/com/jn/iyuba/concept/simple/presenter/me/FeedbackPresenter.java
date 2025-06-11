package com.jn.iyuba.concept.simple.presenter.me;

import com.jn.iyuba.concept.simple.model.bean.me.SuggestBean;
import com.jn.iyuba.concept.simple.model.me.FeedbackModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.me.FeedbackContract;

import io.reactivex.disposables.Disposable;

public class FeedbackPresenter extends BasePresenter<FeedbackContract.FeedbackView, FeedbackContract.FeedbackModel>
        implements FeedbackContract.FeedbackPresenter {


    @Override
    protected FeedbackContract.FeedbackModel initModel() {
        return new FeedbackModel();
    }

    @Override
    public void suggest(int protocol, int uid, String content, String email, String app, String platform) {

        Disposable disposable = model.suggest(protocol, uid, content, email, app, platform, new FeedbackContract.SuggestCallback() {
            @Override
            public void success(SuggestBean suggestBean) {

                view.suggestComplete();
            }

            @Override
            public void error(Exception e) {

                view.toast("请求超时");
            }
        });
        addSubscribe(disposable);
    }
}
