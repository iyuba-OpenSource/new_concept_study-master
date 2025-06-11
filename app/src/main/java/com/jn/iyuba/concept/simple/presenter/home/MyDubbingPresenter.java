package com.jn.iyuba.concept.simple.presenter.home;


import com.jn.iyuba.concept.simple.model.bean.DelEvalBean;
import com.jn.iyuba.concept.simple.model.bean.MyDubbingBean;
import com.jn.iyuba.concept.simple.model.home.MyDubbingModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.MyDubbingContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class MyDubbingPresenter extends BasePresenter<MyDubbingContract.MyDubbingView, MyDubbingContract.MyDubbingModel>
        implements MyDubbingContract.MyDubbingPresenter {

    @Override
    protected MyDubbingContract.MyDubbingModel initModel() {
        return new MyDubbingModel();
    }

    @Override
    public void getTalkShowOtherWorks(int appid, String uid, String appname) {

        Disposable disposable = model.getTalkShowOtherWorks(appid, uid, appname, new MyDubbingContract.Callback() {
            @Override
            public void success(MyDubbingBean myDubbingBeans) {

                if (myDubbingBeans.isResult()) {

                    view.getMyDubbingComplete(myDubbingBeans);
                } else {

                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void delEvalAndDubbing(int protocol, int id) {

        Disposable disposable = model.delEvalAndDubbing(protocol, id, new MyDubbingContract.DelCallback() {
            @Override
            public void success(DelEvalBean delEvalBean) {

                if (delEvalBean.getResultCode().equals("001")) {

                    view.delEvalComplete(id);
                } else {

                    view.toast(delEvalBean.getMessage());
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
