package com.jn.iyuba.concept.simple.model.me;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.me.SuggestBean;
import com.jn.iyuba.concept.simple.view.me.FeedbackContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FeedbackModel implements FeedbackContract.FeedbackModel {


    @Override
    public Disposable suggest(int protocol, int uid, String content, String email, String app, String platform, FeedbackContract.SuggestCallback suggestCallback) {

        return NetWorkManager
                .getRequest()
                .suggest(Constant.PROTOCOL, protocol, uid, content, email, app, platform)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SuggestBean>() {
                    @Override
                    public void accept(SuggestBean suggestBean) throws Exception {

                        suggestCallback.success(suggestBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        suggestCallback.error((Exception) throwable);
                    }
                });
    }
}
